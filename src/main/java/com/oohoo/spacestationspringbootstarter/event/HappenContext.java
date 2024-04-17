package com.oohoo.spacestationspringbootstarter.event;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 事件接收上下文
 *               将事件源头与事件接收进行绑定后存放至内存中
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/5
 */
@Component
public class HappenContext {


    private final Map<String, List<TriggerMethod>> triggerMethods = new HashMap<>();
    private HappenContext() {}

   private static class SingletonHappenContext {
        private static final HappenContext INSTANCE = new HappenContext();
   }

    public static void registerTrigger(TriggerMethod triggerMethod) {
        List<TriggerMethod> triggerMethods = SingletonHappenContext.INSTANCE.triggerMethods.get(triggerMethod.getHappenName());
        if(CollectionUtils.isEmpty(triggerMethods)) {
            triggerMethods = new ArrayList<>();
        }
        triggerMethods.add(triggerMethod);
        SingletonHappenContext.INSTANCE.triggerMethods.put(triggerMethod.getHappenName(),triggerMethods);
    }

    public static List<TriggerMethod> getTriggerMethods(String happenName) {
        return SingletonHappenContext.INSTANCE.triggerMethods.get(happenName);
    }

}
