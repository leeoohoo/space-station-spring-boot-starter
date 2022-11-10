package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.lang.annotation.Annotation;
import java.util.List;

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
        this.bulidSelectSql();
        this.bulidJoinSql();
        this.bulidCdnSql();
        return this.sql.toString();
    }

    @Override
    public List<Object> getParams() {
        return this.sqlContext.getParams();
    }

    private void bulidCdnSql() {
        StringBuilder cdn = this.sqlContext.getCdn();
        this.sqlContext.addBracket(cdn);
        cdn = this.sqlContext.getCdn();

        if (null == cdn) {
            // todo 通过dto 的注解生成
            return;
        }

        this.sql.append(" ").append(cdn);
    }

    private void bulidJoinSql() {
        StringBuilder join = this.sqlContext.getJoin();
        if(null == join) {
            // todo 通过DTO 的注解生成
            return;
        }
        this.sql.append(" ").append(join);
    }


    private void bulidSelectSql() {
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
