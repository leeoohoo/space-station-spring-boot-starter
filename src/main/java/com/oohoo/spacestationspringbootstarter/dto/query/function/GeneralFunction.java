package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.SqlFunctionEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import org.springframework.util.Assert;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
public abstract class GeneralFunction {

    protected SqlFunctionEnum sqlFunctionEnum;
    protected String alias;
    protected String funcSql;

    protected GeneralFunction(SqlFunctionEnum sqlFunctionEnum,String funcSql, String alias ) {
        this.sqlFunctionEnum = sqlFunctionEnum;
        this.alias = alias;
        this.funcSql = funcSql;
    }

    public static GeneralFunction create(SqlFunctionEnum sqlFunctionEnum, String funcSql, String alias) {
        switch (sqlFunctionEnum.getType()) {
            case "MATH":
                return new MathSqlFunction(sqlFunctionEnum,funcSql,alias);
            case "STRING":
                return new StringSqlFunction(sqlFunctionEnum,funcSql,alias);
            case "DATE":
                return new DateSqlFunction(sqlFunctionEnum,funcSql,alias);
            case "CDN":
                return new CdnSqlFunction(sqlFunctionEnum,funcSql,alias);
            default:
                throw new DtoQueryException("sql函数创建发生异常");
        }
    }


}
