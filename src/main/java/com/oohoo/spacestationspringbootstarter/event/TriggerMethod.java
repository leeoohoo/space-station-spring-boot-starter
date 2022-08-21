package com.oohoo.spacestationspringbootstarter.event;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Description: 接收器对象
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/8
 */
@Data
public class TriggerMethod {

    private String name;

    private String happenName;

    private Integer order;

    private Boolean async;

    private String targetName;

    private Boolean isInterface;

    private Method method;

    private Class<?>[] objectTypes;


}
