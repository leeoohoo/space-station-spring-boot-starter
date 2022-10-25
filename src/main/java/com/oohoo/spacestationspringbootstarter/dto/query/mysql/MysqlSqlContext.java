package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.SqlContext;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;

import java.util.ArrayList;
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

    private List<Object> params = new ArrayList<>();

    private final LogicEnum logicEnum = LogicEnum.AND;

    private  LogicEnum temporaryLogicEnum;

    private boolean isFirstCdn = true;


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

    @Override
    public void setSelect(StringBuilder select) {

    }

    @Override
    public void setCdn(StringBuilder cdn) {

    }

    @Override
    public void setLogicEnum(LogicEnum logicEnum) {
        this.temporaryLogicEnum = logicEnum;
    }

    @Override
    public void addCdn(String cdn) {
        if(!isFirstCdn) {
            if(null != this.temporaryLogicEnum) {
                this.cdnSql.append( this.temporaryLogicEnum.getValue());
                this.temporaryLogicEnum = null;
            }
            this.cdnSql.append(this.logicEnum.getValue());
        }
        this.cdnSql.append(cdn);
        this.isFirstCdn = false;
    }



    @Override
    public void setSql(StringBuilder sql) {

    }

    @Override
    public void addParams(Object param) {
        this.params.add(param);
    }
}
