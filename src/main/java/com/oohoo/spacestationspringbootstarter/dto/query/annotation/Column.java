package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 17986
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";

    int width() default 55;

    int decimalBeforWidth() default 10;

    int decimalAfterWidth() default 5;

    boolean notNul() default true;

    boolean notUnique() default true;

    String comment() default "";

}
