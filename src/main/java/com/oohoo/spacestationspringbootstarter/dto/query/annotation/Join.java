package com.oohoo.spacestationspringbootstarter.dto.query.annotation;



import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

import java.lang.annotation.*;

/**
 * @author 17986
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Join.List.class)
public @interface Join{
    JoinEnum join() default JoinEnum.INNER;

    Class<?> fromClazz();

    String fromField();

    OpEnum op() default OpEnum.EQ;

    Class<?> joinClazz();

    String joinField();

    int order() default 0;

    String and() default "";

    String or() default "";

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Join[] value();
    }

}
