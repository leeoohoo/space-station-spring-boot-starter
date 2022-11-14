package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface JoinManager extends SqlManager {

    JoinManager inner(Class<?> clazz, String... alias);

    JoinManager left(Class<?> clazz, String... alias);

    JoinManager right(Class<?> clazz, String... alias);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1);


    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias, OpEnum opEnum, SelectColumn<J, ?> column1);

    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias, OpEnum opEnum, SelectColumn<J, ?> column1, String alias1);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, String alias);

    <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, CdnContainer... condition);

    CdnManager where();

    <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                          OpEnum opEnum,
                          SelectColumn<J, ?> column1, String alias1,
                          CdnContainer... condition);
}
