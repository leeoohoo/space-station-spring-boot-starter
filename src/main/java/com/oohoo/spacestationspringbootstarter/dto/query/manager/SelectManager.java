package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.SqlFunction;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface SelectManager {

    /**
     * 自定义查询字段
     * @param columns 查询字段
     * @return 返回链式调用的结果
     * @param <T> 实体类
     */
    <T> SelectManager select(SelectColumn<T, ?>... columns);

    <T> SelectManager select(SelectColumn<T, ?> column, String alias);

    SelectManager select(Class<?> dtoClazz);

    SelectManager select(GeneralFunction... sqlFunction);

    SelectManager select(GroupByFunction... sqlFunction);


    CdnManager where();

    HavingManager having();

    OrderByManager order();

    JoinManager inner(Class<?> clazz, String... alias);

    JoinManager left(Class<?> clazz, String... alias);

    JoinManager right(Class<?> clazz, String... alias);
}
