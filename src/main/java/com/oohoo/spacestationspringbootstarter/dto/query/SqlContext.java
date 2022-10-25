package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
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

    /**
     * 获取整条sql
     *
     * @return
     */
    StringBuilder getSql();

    /**
     * 获取参数
     *
     * @return
     */
    List<Object> getParams();

    void setSelect(StringBuilder select);

    void setCdn(StringBuilder cdn);

    void addJoin(JoinEnum joinEnum, Class<?> clazz,String... alias);

    void addOn(Column column, OpEnum opEnum, Column column1);

    void addOn(Column column, OpEnum opEnum, Column column1, Condition... condition);

    void setLogicEnum(LogicEnum logicEnum);

    void setBracket();

    void addCdn(String cdn);

    void setSql(StringBuilder sql);

    void addParams(Object param);
}
