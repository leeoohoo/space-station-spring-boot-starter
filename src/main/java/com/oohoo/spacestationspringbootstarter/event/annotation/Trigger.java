package com.oohoo.spacestationspringbootstarter.event.annotation;

import java.lang.annotation.*;

/**
 * @Description: 事件接收器，在发生源被调用后，会根据happenName进行匹配，
 *               依次调用被该注解注释的方法，
 *
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/5
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Trigger {

    String value();

    String happenName();


    boolean async() default false;


    int order() default 0;

}
