package com.oohoo.spacestationspringbootstarter.dto.query;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sql 容器
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
@Component
public interface SqlContext {

    /**
     * 获取select语句
     * @return
     */
    StringBuilder getSelect();

    /**
     * 获取join 语句
     * @return
     */
    StringBuilder getJoin();

    /**
     * 获取where 条件语句
     * @return
     */
    StringBuilder getCdn();

    /**
     * 获取整条sql
     * @return
     */
    StringBuilder getSql();

    /**
     * 获取参数
     * @return
     */
    List<Object> getParams();

    void setSelect(StringBuilder select);

    void setCdn(StringBuilder cdn);

    void addCdn(String cdn);

    void setSql(StringBuilder sql);

    void addParams(Object param);
}
