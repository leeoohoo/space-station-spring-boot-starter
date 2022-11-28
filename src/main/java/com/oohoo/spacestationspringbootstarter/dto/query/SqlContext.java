package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sql 容器
 *
 * @author Lei Li. lei.d.li@capgemini.com
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

    void addJoin(JoinEnum joinEnum, String tableName,String alias);

    void addOn(Column column, OpEnum opEnum, Column column1);

    void addOn(Column column, OpEnum opEnum, Object object);

    void addOn(Column column, OpEnum opEnum, Column column1, CdnContainer... condition);

    void setLogicEnum(LogicEnum logicEnum);

    void setBracket();

    void setHavBracket();

    void addCdn(String cdn);

    void addHaving(String having);

    void setSql(StringBuilder sql);

    void addParams(Object param);

    void setFrom(Class<?> clazz);

    void addBracket(StringBuilder sb);

    void addHavBracket(StringBuilder sb);

    void addLogic(StringBuilder sb);
}
