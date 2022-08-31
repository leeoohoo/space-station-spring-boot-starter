package com.oohoo.spacestationspringbootstarter.event;

import com.google.gson.Gson;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public class EventUtil {
    public static CirculationRecord initCirculationRecord(Object[] params,
                                                    TriggerMethod triggerMethod,
                                                    String errorMsg, Object result,
                                                    EventEnum status) {
        Gson gson = new Gson();
        return CirculationRecord.builder()
                .happenName(triggerMethod.getHappenName())
                .asyncOr(triggerMethod.getAsync() ? EventEnum.ASYNC : EventEnum.SYNCHRONIZE)
                .errorJson(errorMsg)
                .paramsJsonArray(gson.toJson(params))
                .resultJson(gson.toJson(result))
                .triggerName(triggerMethod.getName())
                .step(triggerMethod.getOrder())
                .status(status)
                .type(EventEnum.TRIGGER)
                .build();
    }


    public static CirculationRecord initCirculationRecord(Object[] params,String HappenName,
                                                          String errorMsg, Object result,
                                                          EventEnum status) {

        Gson gson = new Gson();
        return CirculationRecord.builder()
                .happenName(HappenName)
                .errorJson(errorMsg)
                .paramsJsonArray(gson.toJson(params))
                .resultJson(gson.toJson(result))
                .status(status)
                .type(EventEnum.HAPPEN)
                .build();
    }

}
