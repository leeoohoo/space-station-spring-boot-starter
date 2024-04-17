package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sql 容器
 *
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 21 October 2022
 */
@Component
public interface SqlContext {

    /**
     * 获取select语句
     *
     * @return
     */
    StringBuilder getSelect();

    StringBuilder getGeneralFunctionSql();
    StringBuilder getGroupFunctionSql();



    void groupBy();

    Boolean getGroupBy();

    StringBuilder getAlias();

    StringBuilder getGroupAlias();

    /**
     * 获取join 语句
     *
     * @return
     */
    StringBuilder getJoin();

    /**
     * 获取where 条件语句
     *
     * @return
     */
    StringBuilder getCdn();

    StringBuilder getHaving();

    StringBuilder getOrderBySql();

    /**
     * 获取整条sql
     *
     * @return
     */
    Class<?> getFromClass();

    /**
     * 获取参数
     *
     * @return
     */
    List<Object> getParams();

    void setSelect(StringBuilder select);

    void setCdn(StringBuilder cdn);

    void setOrder(StringBuilder order);

    void addJoin(JoinEnum joinEnum, String tableName,String alias);

    void addOn(Column column, OpEnum opEnum, Column column1);

    void addOn(Column column, OpEnum opEnum, Object object);

    void addOn(Column column, OpEnum opEnum, Column column1, CdnContainer... condition);

    void setLogicEnum(LogicEnum logicEnum);

    void setBracket();

    void setHavBracket();

    void addCdn(String cdn);

    void addHaving(String having);

    void addOrderBy(String having);

    void addParams(Object param);

    void setFrom(Class<?> clazz);

    void initSelect();

    void initUpdate();

    void initDelete();

    void addBracket(StringBuilder sb);

    void addHavBracket(StringBuilder sb);

    void addLogic(StringBuilder sb);

    void addAlias(Column column);

    void addAlias(String column);

    Boolean hasSelectField();

    void setSelectField();

    void addSet(String cdnSql);

    StringBuilder getUpdateSql();

    StringBuilder getDeleteSql();

    void validBracket();
}
