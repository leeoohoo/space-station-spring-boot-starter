package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public interface DtoQuery<T,R> {

    /**
     * 获得查询语句
     * @return
     */
    default String select() {
        Class<? extends DtoQuery> aClass = this.getClass();
        Annotation[] annotations = aClass.getDeclaredAnnotations();

        return "";
    }

    default String join() {
        Class<? extends DtoQuery> aClass = this.getClass();
        Join[] joins = aClass.getAnnotationsByType(Join.class);

        return "";
    }

    default void test(R select) {

        String a = (String) select;
        System.out.println(a);
    }


}
