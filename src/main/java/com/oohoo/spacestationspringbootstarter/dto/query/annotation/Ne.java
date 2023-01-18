package com.oohoo.spacestationspringbootstarter.dto.query.annotation;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

import java.lang.annotation.*;

/**
 * @author 17986
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Condition(op = OpEnum.NE)
public @interface Ne {
    LogicEnum logic() default LogicEnum.AND;

    int order() default 0;

    boolean required() default false;

    String key() default "";

    Class<?> table() default ClazzIsNull.class;


}
