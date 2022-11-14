package com.oohoo.spacestationspringbootstarter.dto.query.annotation;



import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author 17986
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Join.List.class)
@Indexed
public @interface Join{
    JoinEnum join() default JoinEnum.INNER;

    Class<?> fromClazz();

    String fromField();

    OpEnum op() default OpEnum.EQ;

    Class<?> joinClazz();

    String joinField();

    int order() default 0;

    /**
     * 不推荐使用
     * @return
     */
    String andSql() default "";

    /**
     * 不推荐使用
     * @return
     */
    String orSql() default "";

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Join[] value();
    }

}
