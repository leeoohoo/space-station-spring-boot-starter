package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import java.lang.annotation.*;

/**
 * @author 17986
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Bracket.List.class)
public @interface Bracket {
    String[] fields();

    int order() default 0;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Bracket[] value();
    }

}
