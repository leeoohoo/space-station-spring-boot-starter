package com.oohoo.spacestationspringbootstarter.event;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Description: 接收器对象
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/8
 */
@Data
public class TriggerMethod {

    /**
     * 当前接收器的名字
     */
    private String name;

    /**
     * 事件发生源的名字
     */
    private String happenName;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 是否异步
     */
    private Boolean async;

    /**
     * 当前代理的类名
     */
    private String targetName;

    /**
     * 当前代理的类是否是接口
     */
    private Boolean isInterface;

    /**
     * 当前代理的方法
     */
    private Method method;

    /**
     * 参数类型
     */
    private Class<?>[] objectTypes;


}
