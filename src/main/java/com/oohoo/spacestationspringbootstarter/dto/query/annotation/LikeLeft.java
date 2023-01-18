package com.oohoo.spacestationspringbootstarter.dto.query.annotation;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LikeLocation;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 17986
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Condition(op = OpEnum.LIKE)
public @interface LikeLeft {
    LogicEnum logic() default LogicEnum.AND;

    LikeLocation likeLocation() default LikeLocation.LEFT;

    int order() default 0;

    boolean required() default false;

    String key() default "";

    Class<?> table() default ClazzIsNull.class;
}
