package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;

/**
 * @author Lei Li. lei.d.li@capgemini.com

 * @since 21 October 2022
 */

public interface SelectManager extends SqlManager {

    /**
     * 自定义查询字段
     *
     * @param columns 查询字段
     * @param <T>     实体类
     * @return 返回链式调用的结果
     */
    @SuppressWarnings("unchecked")
    <T> SelectManager select(SelectColumn<T, ?>... columns);

    /**
     * 指定查询字段并给与别名
     *
     * @param column
     * @param alias
     * @param <T>
     * @return
     */
    <T> SelectManager select(SelectColumn<T, ?> column, String alias);

    /**
     * 将DTO 中的字段作为查询的字段
     * @param dtoClazz
     * @return
     */
    SelectManager select(Class<?> dtoClazz);

    /**
     * 添加一般的函数
     * @param sqlFunction
     * @return
     */
    SelectManager select(GeneralFunction... sqlFunction);

    /**
     * 添加聚合函数
     * @param sqlFunction
     * @return
     */
    SelectManager select(GroupByFunction... sqlFunction);


    /**
     * 添加搜索条件
     * @return
     */
    CdnManager where();

    /**
     * 添加聚合搜索条件
     * @param groupByFunction
     * @param opEnum
     * @param groupByFunction1
     * @return
     * @param <T>
     */
    <T> HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, GroupByFunction groupByFunction1);

    /**
     * 添加聚合搜索条件
     * @return
     */
    HavingManager having();

    /**
     * 添加排序规则
     * @return
     */
    OrderByManager order();

    /**
     * 添加内连接
     * @param clazz
     * @param alias
     * @return
     */
    JoinManager inner(Class<?> clazz);

    /**
     * 添加左链接
     * @param clazz
     * @param alias
     * @return
     */
    JoinManager left(Class<?> clazz);

    /**
     * 添加右链接
     * @param clazz
     * @param alias
     * @return
     */
    JoinManager right(Class<?> clazz);


}
