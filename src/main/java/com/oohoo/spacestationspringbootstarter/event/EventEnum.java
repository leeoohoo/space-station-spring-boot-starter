package com.oohoo.spacestationspringbootstarter.event;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/23
 */
public enum EventEnum {
    /**
     * 成功失败
     */
    SUCCESS(100, "SUCCESS"),
    FAILED(101, "FAILED"),

    /**
     * 是否异步
     */
    ASYNC(200, "ASYNC"),
    SYNCHRONIZE(201, "SYNCHRONIZE"),

    /**
     * 事件源 & 事件接受器
     */
    HAPPEN(300,"HAPPEN"),
    TRIGGER(301,"TRIGGER"),
    ;

    private final Integer code;
    private final String name;

    EventEnum(Integer code,
              String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
