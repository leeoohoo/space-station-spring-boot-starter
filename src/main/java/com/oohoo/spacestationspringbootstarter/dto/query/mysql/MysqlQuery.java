package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.lang.annotation.Annotation;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public  class MysqlQuery extends AbstractSqlQuery {



    @Override
    public void initContext() {
        this.sqlContext = new MysqlSqlContext();
    }



    public String join() {
        Join[] joins = clazz.getAnnotationsByType(Join.class);
        return "";
    }

    public <T> void  test( SelectColumn<T,?> select) {
        SerializedLambda resolve = SerializedLambda.resolve(select);
        String implMethodName = resolve.getImplMethodName();
        System.out.println(resolve);
    }



}
