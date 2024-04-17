package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description 排除非数据库的字段
 * @since 14 November 2022
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
}
