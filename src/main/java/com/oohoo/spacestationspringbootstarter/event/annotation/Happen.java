package com.oohoo.spacestationspringbootstarter.event.annotation;

import java.lang.annotation.*;

/**
 * @Description: 事件发生源
 *               将该注解标识到某一方法后，在该方法被执行时可以通知相应的事件接收器，
 *               并接收发生源方法的入参和返回值参数
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Happen {
    String value();

    boolean enabledSave() default false;
}
