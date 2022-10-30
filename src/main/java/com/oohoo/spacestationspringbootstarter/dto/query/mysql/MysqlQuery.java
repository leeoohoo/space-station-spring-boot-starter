package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.lang.annotation.Annotation;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public class MysqlQuery extends AbstractSqlQuery {

    private StringBuilder sql;

    @Override
    public void initContext() {
        this.sqlContext = new MysqlSqlContext();
    }


    @Override
    public String getSql() {
        this.selectSql();
        return this.sql.toString();
    }


    private void selectSql() {
        StringBuilder select = this.sqlContext.getSelect();
        if (null == select) {
            // todo 通过 dto的字段生成
            return;
        }
        select.deleteCharAt(select.lastIndexOf(","));
        select.append(" from ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()))
                .append(" as ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()));
        this.sql = select;

    }


    @Override
    public MysqlQuery fnish() {
        return (MysqlQuery)this;
    }
}
