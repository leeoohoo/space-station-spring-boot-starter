package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OrderByEnum;

import java.lang.annotation.*;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 30 November 2022
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OrderBy.List.class)
public @interface OrderBy {

    Class<?> table();

    String field();

    OrderByEnum orderType() default OrderByEnum.ASC;

    int order() default 0;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        OrderBy[] value();
    }
}
