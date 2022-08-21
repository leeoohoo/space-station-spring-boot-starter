package com.oohoo.spacestationspringbootstarter.event.annotation;

import com.oohoo.spacestationspringbootstarter.event.HappenFunctionRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description: 开启事件流扫描
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HappenFunctionRegister.class)
public @interface EnableHappenProxy {
    String value() default "";
}
