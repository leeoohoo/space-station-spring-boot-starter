package com.oohoo.spacestationspringbootstarter.validate.annotation;


import com.oohoo.spacestationspringbootstarter.validate.MyValidator;
import com.oohoo.spacestationspringbootstarter.validate.Validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/3
 */
@Documented
@Constraint(
        validatedBy = {MyValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyValidate.List.class)
public @interface MyValidate {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        MyValidate[] value();
    }

    Class<? extends Validate> verify() default Validate.class;


    String groupName() default "";

    int groupSize() default 0;







}
