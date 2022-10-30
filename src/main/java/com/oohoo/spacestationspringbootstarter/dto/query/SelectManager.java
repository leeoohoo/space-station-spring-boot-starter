package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

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


    CdnManager where();

    JoinManager inner(Class<?> clazz, String... alias);

    JoinManager left(Class<?> clazz, String... alias);

    JoinManager right(Class<?> clazz, String... alias);
}
