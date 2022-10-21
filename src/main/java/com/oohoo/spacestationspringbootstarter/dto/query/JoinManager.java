package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface JoinManager extends SqlManager{

    JoinManager inner(Class<?> clazz);

    JoinManager left(Class<?> clazz);

    JoinManager right(Class<?> clazz);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1);


    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, Condition... condition);
    CdnManager where();
}
