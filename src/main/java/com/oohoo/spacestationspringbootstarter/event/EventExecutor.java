package com.oohoo.spacestationspringbootstarter.event;

import com.oohoo.spacestationspringbootstarter.config.SpaceStationAutoConfiguration;
import com.oohoo.spacestationspringbootstarter.config.SpringUtils;
import com.oohoo.spacestationspringbootstarter.event.annotation.Happen;
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

    public final Executor executor = (Executor) SpringUtils.getBean(SpaceStationAutoConfiguration.TASK_EXECUTOR_NAME);

    private EventExecutor() {
    }


    private static class SingletonEventExecutor {
        private static final EventExecutor INSTANCE = new EventExecutor();
    }

    public static EventExecutor getInstance() {
        return SingletonEventExecutor.INSTANCE;
    }


    public void execute(TriggerMethod triggerMethod, Object[] objects,
                        Object[] parameters, Object bean, Happen annotation,
                        RecordAbstract recordAbstract) {
        Task task = new Task(objects, parameters, triggerMethod, bean, annotation, recordAbstract);
        if (null != SingletonEventExecutor.INSTANCE.executor) {
            SingletonEventExecutor.INSTANCE.executor.execute(task);
        } else {
            log.warn("[事件接收]--------->>>>>>>请配置线程池的名字，station.taskExecutorName");
            new Thread(task).start();
        }
    }


    private class Task implements Runnable {
        public Task(Object[] objects, Object[] parameters,
                    TriggerMethod triggerMethod, Object bean, Happen annotation,
                    RecordAbstract recordAbstract) {
            this.args = objects;
            this.triggerMethod = triggerMethod;
            this.parameters = parameters;
            this.bean = bean;
            this.annotation = annotation;
            this.recordAbstract = recordAbstract;
        }

        Object[] args;

        Object[] parameters;

        Object bean;

        TriggerMethod triggerMethod;

        Happen annotation;

        RecordAbstract recordAbstract;

        @Override
        public void run() {
            EventThreadValue.setParamsThreadLocal(Arrays.asList(parameters));
            CirculationRecord circulationRecord = null;
            try {
                Object invoke = triggerMethod.getMethod().invoke(bean, args);
                EventThreadValue.setStepResultThreadLocal(triggerMethod.getTargetName(), invoke);
                if(annotation.enabledSave()) {
                    circulationRecord =
                            EventUtil.initCirculationRecord(this.args, this.triggerMethod, "", invoke, EventEnum.SUCCESS);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                if(annotation.enabledSave()) {
                    circulationRecord =
                            EventUtil.initCirculationRecord(this.args, triggerMethod, e.getMessage(), null,EventEnum.FAILED);
                }
                throw new RuntimeException(e);
            }finally {
                if(annotation.enabledSave()) {
                    recordAbstract.saveTrigger(circulationRecord);
                }
            }
            EventThreadValue.clearThreadLocal();
        }
    }


}
