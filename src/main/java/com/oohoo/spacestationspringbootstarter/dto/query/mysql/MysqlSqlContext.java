package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.SqlContext;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;

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

    private final StringBuilder joinSql = new StringBuilder();

    private final StringBuilder cdnSql = new StringBuilder();

    private final List<Object> params = new ArrayList<>();

    private final LogicEnum logicEnum = LogicEnum.AND;

    private LogicEnum temporaryLogicEnum;

    private boolean isFirstCdn = true;

    private Integer bracketCount = 0;

    private boolean temporaryBracket = false;


    @Override
    public StringBuilder getSelect() {
        if (null == this.selectSql) {
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
        return this.cdnSql;
    }

    @Override
    public StringBuilder getSql() {
        return null;
    }

    @Override
    public List<Object> getParams() {
        return this.params;
    }

    @Override
    public void setSelect(StringBuilder select) {

    }

    @Override
    public void setCdn(StringBuilder cdn) {

    }

    @Override
    public void addJoin(JoinEnum joinEnum, Class<?> clazz, String... alias) {
        String tableName = ClassUtils.getTableName(clazz);
        this.joinSql.append(joinEnum.getType()).append(tableName);
        if (null != alias && alias.length > 0) {
            this.joinSql.append(" as ").append(alias[0]);
        }else {
            this.joinSql.append(" as ").append(tableName);
        }
    }

    @Override
    public void addOn(Column column, OpEnum opEnum, Column column1) {

    }

    @Override
    public void addOn(Column column, OpEnum opEnum, Column column1, Condition... condition) {

    }

    @Override
    public void setLogicEnum(LogicEnum logicEnum) {
        this.temporaryLogicEnum = logicEnum;
    }

    @Override
    public void setBracket() {
        this.bracketCount++;
        this.temporaryBracket = true;
    }

    @Override
    public void addCdn(String cdn) {
        if (!isFirstCdn) {
            if (null != this.temporaryLogicEnum) {
                this.cdnSql.append(this.temporaryLogicEnum.getValue());
                this.temporaryLogicEnum = null;
            }
            this.cdnSql.append(this.logicEnum.getValue());
        }
        if (this.bracketCount % 2 != 0 && this.temporaryBracket) {
            this.cdnSql.append(" (");
            this.temporaryBracket = false;
        }
        this.cdnSql.append(cdn);
        if (this.bracketCount % 2 == 0 && this.temporaryBracket) {
            this.cdnSql.append(" )");
            this.temporaryBracket = false;
        }
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
