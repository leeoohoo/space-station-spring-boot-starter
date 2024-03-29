package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.SqlFunctionEnum;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
public class DateSqlFunction extends GeneralFunction {
    public DateSqlFunction(SqlFunctionEnum sqlFunctionEnum, String funcSql, String alias) {
        super(sqlFunctionEnum,funcSql,alias);
    }
}
