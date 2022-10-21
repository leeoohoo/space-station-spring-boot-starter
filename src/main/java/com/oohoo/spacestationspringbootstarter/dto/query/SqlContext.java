package com.oohoo.spacestationspringbootstarter.dto.query;

import java.util.List;

/**
 * Sql 容器
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
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
}
