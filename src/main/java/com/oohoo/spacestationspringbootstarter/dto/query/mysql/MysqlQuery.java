package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;

import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public class MysqlQuery extends AbstractSqlQuery {

    private StringBuilder sql;

    public static MysqlQuery init(){
         return new MysqlQuery();
    }

    private MysqlQuery() {}

    @Override
    public void initContext() {
        this.sqlContext = MysqlSqlContext.init();
    }


    @Override
    public String getSql() {
        this.buildSelectSql();
        this.buildJoinSql();
        this.buildCdnSql();
        return this.sql.toString();
    }

    @Override
    public List<Object> getParams() {
        return this.sqlContext.getParams();
    }

    private void buildCdnSql() {
        StringBuilder cdn = this.sqlContext.getCdn();
        this.sqlContext.addBracket(cdn);
        cdn = this.sqlContext.getCdn();

        if (null == cdn) {
            // todo 通过dto 的注解生成
            return;
        }

        this.sql.append(" ").append(cdn);
    }

    private void buildJoinSql() {
        StringBuilder join = this.sqlContext.getJoin();
        if(null == join) {
            // todo 通过DTO 的注解生成
            return;
        }
        this.sql.append(" ").append(join).append(" \n");
    }


    private void buildSelectSql() {
        StringBuilder select = this.sqlContext.getSelect();
        if (null == select) {
            // todo 通过 dto的字段生成
            return;
        }
        select.deleteCharAt(select.lastIndexOf(","));
        select.append(" from ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()))
                .append(" as ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()))
                .append(" \n");
        this.sql = select;

    }


    @Override
    public MysqlQuery finish() {
        return (MysqlQuery)this;
    }
}
