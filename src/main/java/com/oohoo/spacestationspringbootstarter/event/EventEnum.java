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
    ;

    private final Integer code;
    private final String name;

    EventEnum(Integer code,
            String name){
        this.code = code;
        this.name = name;
    }
}
