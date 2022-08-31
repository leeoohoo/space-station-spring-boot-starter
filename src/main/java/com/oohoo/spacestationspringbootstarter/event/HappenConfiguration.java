package com.oohoo.spacestationspringbootstarter.event;

import com.google.gson.Gson;
import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import com.oohoo.spacestationspringbootstarter.event.annotation.Happen;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/9
 */

@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
public class HappenConfiguration {
    /**
     * 把寻找到的Advice和Pointcut(matches)相互关联(交给StaticMethodMatcherPointcutAdvisor相互关联)
     *
     * @return
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public HappenAdvisor registerHappenAdvisor(HappenMethodInterceptor happenMethodInterceptor) {
        return new HappenAdvisor(happenMethodInterceptor);

    }

    /**
     * 寻找Advice
     *
     * @return
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public HappenMethodInterceptor registerMethodInterceptor() {
        return new HappenMethodInterceptor();
    }


    static class HappenAdvisor extends StaticMethodMatcherPointcutAdvisor {

        /**
         * 注册Advice
         *
         * @param happenMethodInterceptor
         */
        public HappenAdvisor(HappenMethodInterceptor happenMethodInterceptor) {
            this.setAdvice(happenMethodInterceptor);
        }

        /**
         * 检索项目中的LocalRateLimiter注解
         *
         * @param method
         * @param targetClass
         * @return
         */
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return method.isAnnotationPresent(Happen.class);
        }

    }

    /**
     * 事件触发监听
     */
    static class HappenMethodInterceptor implements MethodInterceptor {


        @Autowired
        private RecordAbstract recordAbstract;

        @Nullable
        @Override
        public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {

            boolean annotationPresent = invocation.getMethod().isAnnotationPresent(Happen.class);
            if (!annotationPresent) {
                // 如果不是事件发生的方法则直接返回
                return invocation.proceed();
            }

            Happen annotation = invocation.getMethod().getAnnotation(Happen.class);
            List<TriggerMethod> value = HappenContext.getTriggerMethods(annotation.value());
            Object proceed = this.happen(invocation,annotation);
            if (!CollectionUtils.isEmpty(value)) {
                // 如果发现后续事件的接收者，手动打开事物
                AbstractPlatformTransactionManager transactionManager = SpringUtils.getBean("transactionManager");
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();

                // 事物隔离级别，开启新事务，这样会比较安全些
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                assert transactionManager != null;
                // 获得事务状态
                TransactionStatus status = transactionManager.getTransaction(def);
                // 根据事件接收者的排序字段进行排序 顺序执行
                value.sort(Comparator.comparing(TriggerMethod::getOrder));
                value.stream().filter(it -> !it.getAsync()).forEach(triggerMethod -> {
                    //执行每一个同步的事件接受器
                    synchronizeTrigger(invocation, proceed, triggerMethod, annotation, transactionManager, status);
                });
                // 执行异步的方法 异步的方法不参与排序
                value.stream().filter(TriggerMethod::getAsync).forEach(triggerMethod -> {
                    Object[] arguments = invocation.getArguments();
                    Object[] params = this.initParams(proceed, arguments, triggerMethod.getObjectTypes());
                    Object bean = SpringUtils.getBean(triggerMethod.getTargetName());
                    EventExecutor instance = EventExecutor.getInstance();
                    instance.execute(triggerMethod, params, arguments, bean, annotation, recordAbstract);
                });

                transactionManager.commit(status);
                EventThreadValue.clearThreadLocal();
            }


            return proceed;
        }

        /**
         * 代理事件发生源的方法
         * @param invocation
         * @param annotation
         * @return
         * @throws Throwable
         */
        private Object happen(MethodInvocation invocation,Happen annotation) throws Throwable {
            Object proceed = null;
            CirculationRecord circulationRecord = null;
            try {
                proceed = invocation.proceed();
                if (annotation.enabledSave()) {
                    circulationRecord = EventUtil.initCirculationRecord(invocation.getArguments(),annotation.value(),"",
                            proceed,EventEnum.SUCCESS);
                }
            } catch (Exception e) {
                if (annotation.enabledSave()) {
                    circulationRecord = EventUtil.initCirculationRecord(invocation.getArguments(),annotation.value(),e.getMessage(),
                            null,EventEnum.SUCCESS);
                }
                throw e;
            }finally {
                if(annotation.enabledSave()) {
                    recordAbstract.saveHappen(circulationRecord);
                }
            }
            return proceed;
        }


        /**
         * 执行同步的事件接受器
         * @param invocation 事件发生源的方法
         * @param proceed 事件发生源的返回值
         * @param triggerMethod 事件接收器的方法
         * @param annotation 事件发生源的注解
         * @param transactionManager 事物管理
         * @param status 事物状态
         */
        private void synchronizeTrigger(MethodInvocation invocation, Object proceed,
                                        TriggerMethod triggerMethod, Happen annotation,
                                        AbstractPlatformTransactionManager transactionManager,
                                        TransactionStatus status) {
            Object[] params = null;
            CirculationRecord circulationRecord = null;
            try {
                Object[] arguments = invocation.getArguments();
                params = this.initParams(proceed, arguments, triggerMethod.getObjectTypes());
                Object bean = null;
                if (triggerMethod.getIsInterface()) {
                    log.warn("[事件接收]----------->>>>>>>class:{}, 为interface, 本组件暂时不支持", triggerMethod.getTargetName());
                } else {
                    bean = SpringUtils.getBean(triggerMethod.getTargetName());
                }
                if (null != bean) {
                    Object invoke = triggerMethod.getMethod().invoke(bean, params);
                    EventThreadValue.setStepResultThreadLocal(triggerMethod.getName(), invoke);
                    if (annotation.enabledSave()) {
                        circulationRecord =
                                EventUtil.initCirculationRecord(params, triggerMethod, "", invoke, EventEnum.SUCCESS);
                    }
                } else {
                    log.warn("[事件接收]---------->>>>>>class:{},未找到或未加入到IOC管理，未能触发事件接收", triggerMethod.getTargetName());
                }

            } catch (Exception e) {
                if (annotation.enabledSave()) {
                    circulationRecord =
                            EventUtil.initCirculationRecord(params, triggerMethod, e.getMessage(), null, EventEnum.FAILED);
                }
                transactionManager.commit(status);
                throw new RuntimeException(e);
            } finally {
                if (annotation.enabledSave()) {
                    recordAbstract.saveTrigger(circulationRecord);
                }
            }
        }

        private Object[] initParams(Object result, Object[] objects, Class<?>[] types) {
            Object[] objects1 = {result};
            List<Object> collect = Arrays.stream(objects1).collect(Collectors.toList());
            collect.addAll(Arrays.stream(objects).collect(Collectors.toList()));
            // 将事件发生时的入参值和返回值放到 当前线程变量中，
            // 当事件接收的方法参数与事件发起时的入参不匹配时，在事件接收的方法中可以线程变量中获取
            EventThreadValue.setParamsThreadLocal(Arrays.stream(objects).collect(Collectors.toList()));
            EventThreadValue.setResultThreadLocal(result);
            if (types.length != collect.size()) {

                return new Object[types.length];
            }
            for (int i = 0; i < types.length; i++) {
                if (collect.get(i) != null && collect.get(i).getClass() != types[i]) {

                    return new Object[types.length];
                }
            }
            return collect.toArray();

        }
    }


}
