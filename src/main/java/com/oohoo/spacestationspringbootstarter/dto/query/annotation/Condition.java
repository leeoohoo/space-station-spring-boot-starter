package com.oohoo.spacestationspringbootstarter.dto.query.annotation;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author 17986
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Condition {
    LogicEnum logic() default LogicEnum.AND;

    OpEnum op();

    boolean required() default false;

    int order() default 0;

}
