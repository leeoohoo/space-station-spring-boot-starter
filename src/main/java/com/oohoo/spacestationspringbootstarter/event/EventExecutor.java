package com.oohoo.spacestationspringbootstarter.event;

import com.oohoo.spacestationspringbootstarter.config.SpaceStationAutoConfiguration;
import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/8
 */
@Slf4j
public class EventExecutor {

    public final  Executor executor = (Executor) SpringUtils.getBean(SpaceStationAutoConfiguration.TASK_EXECUTOR_NAME);

    private EventExecutor() {}


    private static class SingletonEventExecutor {

        private static final EventExecutor INSTANCE = new EventExecutor();
    }

    public static EventExecutor getInstance() {
        return SingletonEventExecutor.INSTANCE;
    }



    public void execute(TriggerMethod triggerMethod, Object[] objects, Object result,Object[] parameters, Object bean) {
        Task task = new Task(objects, result, parameters, triggerMethod, bean);
        if(null != SingletonEventExecutor.INSTANCE.executor) {
            SingletonEventExecutor.INSTANCE.executor.execute(task);
        }else {
            log.warn("[事件接收]--------->>>>>>>请配置线程池的名字，station.taskExecutorName");
            new Thread(task).start();
        }
    }


    private  class Task implements Runnable {
        public Task(Object[] objects,Object object,Object[] parameters, TriggerMethod triggerMethod, Object bean) {
            this.args = objects;
            this.result = object;
            this.triggerMethod = triggerMethod;
            this.parameters = parameters;
            this.bean = bean;
        }
        Object[] args;

        Object result;

        Object[] parameters;

        Object bean;

        TriggerMethod triggerMethod;

        @Override
        public void run() {
            EventThreadValue.setParamsThreadLocal(Arrays.asList(parameters));
            EventThreadValue.setResultThreadLocal(result);
            try {
                Object invoke = triggerMethod.getMethod().invoke(bean, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            EventThreadValue.clearThreadLocal();
        }
    }


}
