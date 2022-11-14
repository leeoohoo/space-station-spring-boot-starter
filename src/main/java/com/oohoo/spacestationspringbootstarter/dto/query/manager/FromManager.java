package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 28 October 2022
 */
public interface FromManager {

    FromManager from(Class<?> clazz);

    <T> SelectManager select(SelectColumn<T, ?>... columns);

    <T> SelectManager select(SelectColumn<T, ?> column, String alias);
}
