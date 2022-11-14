package com.oohoo.spacestationspringbootstarter.dto.query.annotation;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.CdnScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.annotation.*;

/**
 * @author 17986
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Condition(op = OpEnum.EQ)
public @interface Eq {
    LogicEnum logic() default LogicEnum.AND;

    @AliasFor(annotation = Condition.class,attribute = "order")
    int order() default 0;

    @AliasFor(annotation = Condition.class,attribute = "required")
    boolean required() default false;



}
