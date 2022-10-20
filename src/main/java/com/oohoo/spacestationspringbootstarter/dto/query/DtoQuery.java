package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public interface DtoQuery<T> extends Serializable {

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

    default void test( SelectColumn<Test,?> select) {
        SerializedLambda resolve = SerializedLambda.resolve(select);
        String implMethodName = resolve.getImplMethodName();
        System.out.println(resolve);
    }

    default String getSql(){
        return "";
    }

    default List<Object> getParam() {
        return null;
    }



}
