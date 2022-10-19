package com.oohoo.spacestationspringbootstarter.dto.query.annotation;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

/**
 * @author 17986
 */
public @interface Condition {
    LogicEnum logic() default LogicEnum.AND;

    OpEnum op();

    boolean required() default false;

    int order() default 0;
}
