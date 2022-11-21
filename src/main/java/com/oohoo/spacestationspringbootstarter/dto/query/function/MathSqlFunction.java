package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
public interface MathSqlFunction extends GeneralFunction{

    <T> MathSqlFunction abs(SelectColumn<T, ?> selectColumn, SelectColumn<T, ?>... alias);
}
