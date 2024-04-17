package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 21 October 2022
 */
public interface JoinManager extends SqlManager {

    JoinManager inner(Class<?> clazz);

    JoinManager left(Class<?> clazz);

    JoinManager right(Class<?> clazz);

    <T, J> JoinManager on(SelectColumn<T, ?> column,  SelectColumn<J, ?> column1);
    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1);


    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias, OpEnum opEnum, SelectColumn<J, ?> column1);

    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias, OpEnum opEnum, SelectColumn<J, ?> column1, String alias1);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, String alias);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, CdnContainer... condition);

    CdnManager where();

    HavingManager having();

    OrderByManager order();

    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                          OpEnum opEnum,
                          SelectColumn<J, ?> column1, String alias1,
                          CdnContainer... condition);
}
