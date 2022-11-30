package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.*;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/31
 */
public class MysqlQuery extends AbstractSqlQuery {

    private StringBuilder sql;

    public static MysqlQuery init() {
        return new MysqlQuery();
    }

    private MysqlQuery() {
    }

    @Override
    public void initContext() {
        this.sqlContext = MysqlSqlContext.init();
    }


    @Override
    public String getSql() {
        this.buildSelectSql();
        this.buildJoinSql();
        this.buildCdnSql();
        this.buildSqlFunctionSql();
        this.buildHavingSql();
        this.buildOrderSql();
        return this.sql.toString();
    }

    private void buildOrderSql() {
        this.sql.append(this.sqlContext.getOrderBySql());
    }

    private void buildHavingSql() {
        StringBuilder having = this.sqlContext.getHaving();
        this.sqlContext.addHavBracket(having);
        having = this.sqlContext.getHaving();
        this.sql.append(" ").append(having);
    }


    @Override
    public List<Object> getParams() {
        return this.sqlContext.getParams();
    }

    private void buildCdnSql() {
        StringBuilder cdn = this.sqlContext.getCdn();
        this.sqlContext.addBracket(cdn);
        cdn = this.sqlContext.getCdn();
        this.sql.append(" ").append(cdn);
    }

    private void buildJoinSql() {
        StringBuilder join = this.sqlContext.getJoin();
        this.sql.append(" ").append(join).append(" \n");
    }

    private void buildSqlFunctionSql() {
        if (!this.sqlContext.getGroupBy()) {
            return;
        }
        this.sql.append(" group by ");
        this.sql.append(this.sqlContext.getAlias()).append(" \n");
    }

    private void buildSelectSql() {
        StringBuilder select = this.sqlContext.getSelect();
        select.append(this.sqlContext.getGeneralFunctionSql());
        select.append(this.sqlContext.getGroupFunctionSql());
        select.deleteCharAt(select.lastIndexOf(","));
        select.append(" from ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()))
                .append(" as ").append(ClassUtils.getTableName(this.sqlContext.getFromClass()))
                .append(" \n");
        this.sql = select;
    }


    @Override
    public MysqlQuery finish() {
        StringBuilder alias = this.sqlContext.getAlias();
        String[] split = alias.toString().split(",");
        Set<String> stringSet = new HashSet<>();
        Arrays.stream(split).map(it ->{
            String[] as = it.split("as");
            return as.length >=2 ? as[1] : as[0];
        }).forEach(stringSet::add);
        if (stringSet.size() != split.length) {
            throw new DtoQueryException("查询的字段重复,e:[" + this.sqlContext.getAlias() + "]");
        }
        return this;
    }
}
