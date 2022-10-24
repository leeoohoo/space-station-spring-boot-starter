package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface SelectManager {

    <T> SelectManager select(SelectColumn<T,?>... columns);

    <T,D> SelectManager select(SelectColumn<T,?> column, String alias);

    CdnManager where();

    JoinManager inner(Class<?> clazz);

    JoinManager left(Class<?> clazz);

    JoinManager right(Class<?> clazz);
}
