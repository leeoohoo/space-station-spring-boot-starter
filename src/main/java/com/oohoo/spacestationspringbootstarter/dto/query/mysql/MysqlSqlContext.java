package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.SqlContext;

import java.util.List;

/**
 * mysql的sql 容器实现
 *
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public class MysqlSqlContext implements SqlContext {
    private StringBuilder selectSql;

    private StringBuilder cdnSql;

    private List<Object> params;


    @Override
    public StringBuilder getSelect() {
        if(null == this.selectSql) {
            this.selectSql = new StringBuilder("select ");
            return this.selectSql;
        }
        return selectSql;
    }

    @Override
    public StringBuilder getJoin() {
        return null;
    }

    @Override
    public StringBuilder getCdn() {
        return null;
    }

    @Override
    public StringBuilder getSql() {
        return null;
    }

    @Override
    public List<Object> getParams() {
        return null;
    }
}
