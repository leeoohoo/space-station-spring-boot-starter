package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.SqlContext;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * mysql的sql 容器实现
 *
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public class MysqlSqlContext implements SqlContext {

    private static final String ON = " on ";

    private Class<?> fromClazz;
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
        this.cdnSql.append(" where ");
    }

    @Override
    public void addJoin(JoinEnum joinEnum, String tableName, String alias) {
        this.joinSql.append(joinEnum.getType()).append(tableName);
        if (StringUtils.hasLength(alias)) {
            this.joinSql.append(" as ").append(alias);
        } else {
            this.joinSql.append(" as ").append(tableName);
        }
    }

    @Override
    public void addOn(Column column, OpEnum opEnum, Column column1) {
        this.joinSql.append(ON);
        this.addOnCdn(column, opEnum, column1);
    }

    @Override
    public void addOn(Column column, OpEnum opEnum, Object object) {
        this.joinSql.append(ON);
        this.addOnCdn(column, opEnum, object);
    }



    @Override
    public void addOn(Column column, OpEnum opEnum, Column column1, Condition... condition) {
        this.addOn(column, opEnum, column1);
        Arrays.stream(condition).forEach(it -> {
            this.joinSql.append(it.getLogicSymbol().getValue());
            if (null == it.getColumn1()) {
                this.addOnCdn(it.getColumn(), it.getOpEnum(), it.getValue());
            } else {
                this.addOnCdn(it.getColumn(), it.getOpEnum(), it.getColumn1());
            }
        });
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
            } else {
                this.cdnSql.append(this.logicEnum.getValue());
            }
        }
        if (this.bracketCount % 2 != 0 && this.temporaryBracket) {
            this.cdnSql.append(" (");
            this.temporaryBracket = false;
        }
        this.cdnSql.append(cdn).append("\n");
        if (this.bracketCount % 2 == 0 && this.temporaryBracket) {
            this.cdnSql.append(" )\n");
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

    private void addOnCdn(Column column, OpEnum opEnum, Object object) {
        this.joinSql.append(column.getOnSql()).append(opEnum.getOp()).append(" ? ");
        this.params.add(object);
    }

    private void addOnCdn(Column column, OpEnum opEnum, Column column1) {
        this.joinSql.append(column.getOnSql()).append(opEnum.getOp()).append(column1.getOnSql());
    }
}
