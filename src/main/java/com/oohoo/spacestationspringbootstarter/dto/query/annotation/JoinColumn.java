package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 17986
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumn {
    Class<?> joinClass();

    String columnName();

    String alias() default "";
}
