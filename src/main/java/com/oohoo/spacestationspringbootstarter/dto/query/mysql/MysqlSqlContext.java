package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
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

    private final StringBuilder generalFunctionSql = new StringBuilder();
    private final StringBuilder groupFunctionSql = new StringBuilder();

    private final StringBuilder joinSql = new StringBuilder();

    private final StringBuilder cdnSql = new StringBuilder();

    private final StringBuilder havingSql = new StringBuilder();

    private final StringBuilder orderBySql = new StringBuilder();

    private Boolean groupBy = false;

    private final StringBuilder alias = new StringBuilder();

    private final StringBuilder groupAlias = new StringBuilder();

    private final List<Object> params = new ArrayList<>();

    private final LogicEnum logicEnum = LogicEnum.AND;

    private LogicEnum temporaryLogicEnum;

    private boolean isFirstCdn = true;

    private boolean isFirstHaving = true;

    private Integer bracketCount = 0;

    private boolean temporaryBracket = false;

    private boolean havTemporaryBracket = false;

    private boolean hasSelectField = false;


    private MysqlSqlContext() {
    }

    public static MysqlSqlContext init() {
        return new MysqlSqlContext();
    }

    @Override
    public StringBuilder getSelect() {
        if (null == this.selectSql) {
            this.selectSql = new StringBuilder("select ");
            return this.selectSql;
        }
        return selectSql;
    }

    @Override
    public StringBuilder getGeneralFunctionSql() {
        return this.generalFunctionSql;
    }

    @Override
    public StringBuilder getGroupFunctionSql() {
        return this.groupFunctionSql;
    }

    @Override
    public void groupBy() {
        this.groupBy = true;
    }

    @Override
    public Boolean getGroupBy() {
        return this.groupBy;
    }

    public StringBuilder getAlias() {
        if(!StringUtils.hasLength(this.alias)) {
            return this.alias;
        }
        if(',' == this.alias.charAt(this.alias.toString().trim().length()-1)){
            return this.alias.deleteCharAt(this.alias.lastIndexOf(","));
        }else {
            return this.alias;
        }

    }

    public StringBuilder getGroupAlias() {
        return this.groupAlias;
    }

    @Override
    public StringBuilder getJoin() {
        return this.joinSql;
    }

    @Override
    public StringBuilder getCdn() {
        return this.cdnSql;
    }

    @Override
    public StringBuilder getHaving() {
        return this.havingSql;
    }

    @Override
    public StringBuilder getOrderBySql() {
        if(StringUtils.hasLength(this.orderBySql)) {
            return this.orderBySql.deleteCharAt(this.orderBySql.lastIndexOf(","));
        }else {
            return this.orderBySql;
        }
    }

    @Override
    public Class<?> getFromClass() {
        return this.fromClazz;
    }


    @Override
    public List<Object> getParams() {
        return this.params;
    }

    @Override
    public void setSelect(StringBuilder select) {

    }

    @Override
    public void setOrder(StringBuilder order) {
        this.orderBySql.append(order);
    }

    @Override
    public void setCdn(StringBuilder cdn) {
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
    public void addOn(Column column, OpEnum opEnum, Column column1, CdnContainer... condition) {
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
    public void setHavBracket() {
        this.bracketCount++;
        this.havTemporaryBracket = true;
    }

    @Override
    public void addCdn(String cdn) {
        if (!isFirstCdn) {
            this.addLogic(this.cdnSql);
        } else {
            this.cdnSql.append(" where ");
        }
        this.addBracket(this.cdnSql);
        this.cdnSql.append(cdn).append("\n");
        this.addBracket(this.cdnSql);
        this.isFirstCdn = false;
    }

    @Override
    public void addHaving(String having) {
        if (!isFirstHaving) {
            this.addLogic(this.havingSql);
        } else {
            this.havingSql.append(" having ");
        }
        this.addHavBracket(this.havingSql);
        this.havingSql.append(having).append("\n");
        this.addHavBracket(this.havingSql);
        this.isFirstHaving = false;
    }


    @Override
    public void addOrderBy(String order) {
        this.orderBySql.append(order).append(", ");
    }

    @Override
    public void addParams(Object param) {
        if (null != param) {
            this.params.add(param);
        }
    }

    @Override
    public void setFrom(Class<?> clazz) {
        this.fromClazz = clazz;
    }

    private void addOnCdn(Column column, OpEnum opEnum, Object object) {
        this.joinSql.append(column.getOnSql()).append(opEnum.getOp()).append(" ? ").append("\n");
        this.params.add(object);
    }

    private void addOnCdn(Column column, OpEnum opEnum, Column column1) {
        this.joinSql.append(column.getOnSql()).append(opEnum.getOp()).append(column1.getOnSql()).append("\n");
    }

    public void addBracket(StringBuilder sb) {
        if (this.bracketCount % 2 != 0 && this.temporaryBracket) {
            sb.append(" (");
            this.temporaryBracket = false;
        }
        if (this.bracketCount % 2 == 0 && this.temporaryBracket) {
            sb.append(" )\n");
            this.temporaryBracket = false;
        }
    }

    @Override
    public void addHavBracket(StringBuilder sb) {
        if (this.bracketCount % 2 != 0 && this.havTemporaryBracket) {
            sb.append(" (");
            this.havTemporaryBracket = false;
        }
        if (this.bracketCount % 2 == 0 && this.havTemporaryBracket) {
            sb.append(" )\n");
            this.havTemporaryBracket = false;
        }
    }

    public void addLogic(StringBuilder sb) {
        if (null != this.temporaryLogicEnum) {
            sb.append(this.temporaryLogicEnum.getValue());
            this.temporaryLogicEnum = null;
        } else {
            sb.append(this.logicEnum.getValue());
        }
    }

    @Override
    public void addAlias(Column column) {
        this.alias.append(column.getTableName()).append(".").append(column.getField()).append(" as ").append(column.getAlias()).append(", ");
    }

    @Override
    public void addAlias(String column) {
        this.alias.append(column).append(", ");
    }

    @Override
    public Boolean hasSelectField() {
        return this.hasSelectField;
    }

    @Override
    public void setSelectField(){
        this.hasSelectField = true;
    }
}
