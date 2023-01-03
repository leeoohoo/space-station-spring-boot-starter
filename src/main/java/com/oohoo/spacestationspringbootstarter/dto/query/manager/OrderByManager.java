package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OrderByEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 30 November 2022
 */
public interface OrderByManager extends SqlManager {

    <T> OrderByManager by(SelectColumn<T, ?> column, OrderByEnum order);

    <T> OrderByManager by(SelectColumn<T, ?> column);

    <T> OrderByManager by(OrderByEnum order, SelectColumn<T, ?>... columns);

}
