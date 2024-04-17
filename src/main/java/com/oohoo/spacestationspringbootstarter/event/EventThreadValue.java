package com.oohoo.spacestationspringbootstarter.event;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/8
 */
@Component
public class EventThreadValue {

    /**
     * 字段列表
     */
    private static final ThreadLocal<List<Object>> PARAMS =  new ThreadLocal<>();
    private static final ThreadLocal<Object> RETURN =  new ThreadLocal<>();

    private static final ThreadLocal<Map<String,Object>> STEP_RETURN = new ThreadLocal<>();

    /**
     * 设置事件发生的入参变量
     * @param params
     */
    public static void setParamsThreadLocal(List<Object> params) {
        PARAMS.set(params);
    }

    /**
     * 设置事件发生的结果变量
     * @param result
     */
    public static void setResultThreadLocal(Object result) {
        RETURN.set(result);
    }

    /**
     * 设置事件发生的结果变量
     * @param key
     * @param value
     */
    public static void setStepResultThreadLocal(String key, Object value) {
        Map<String, Object> stringObjectMap = STEP_RETURN.get();
        if(null == stringObjectMap) {
            stringObjectMap = new HashMap<>();
        }
        stringObjectMap.put(key, value);
        STEP_RETURN.set(stringObjectMap);
    }



    /**
     * 取得当前线程中的变量
     */
    public static List<Object> getParamsThreadLocal() {
        return PARAMS.get();
    }

    /**
     * 取得当前线程中的变量
     */
    public static Object getResultThreadLocal() {
        return RETURN.get();
    }

    /**
     * 取得当前线程中的变量
     */
    public static Object getStepResultThreadLocal(String key) {
        Map<String, Object> stringObjectMap = STEP_RETURN.get();
        if(null == stringObjectMap){
            return null;
        }
        return stringObjectMap.get(key);
    }

    /**
     * 清除当前线程用户
     */
    public static void clearThreadLocal() {
        RETURN.remove();
        PARAMS.remove();
        STEP_RETURN.remove();
    }
}
