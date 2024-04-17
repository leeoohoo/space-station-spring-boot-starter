package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.SqlFunctionEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import lombok.Data;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 21 November 2022
 */
@Data
public class GroupByFunction implements SqlFunction {

    private GeneralFunction generalFunction;

    private SqlFunctionEnum sqlFunctionEnum;

    private String alias;

    private String funcSql;

    private Boolean distinct;


    private GroupByFunction(GeneralFunction generalFunction, SqlFunctionEnum sqlFunctionEnum,
                            String funcSql, String alias) {
        this.generalFunction = generalFunction;
        this.sqlFunctionEnum = sqlFunctionEnum;
        this.alias = alias;
        this.funcSql = funcSql;
        this.distinct = false;
    }

    public String getFuncSql() {
        if (!distinct) {
            this.funcSql = this.funcSql.replace("distinct", "");
        }
        return this.funcSql;
    }

    public static GroupByFunction create(GeneralFunction generalFunction, SqlFunctionEnum sqlFunctionEnum, String funcSql, String alias) {
        switch (sqlFunctionEnum.getType()) {
            case "AGGREGATE":
                return new GroupByFunction(generalFunction, sqlFunctionEnum, funcSql, alias);
            default:
                throw new DtoQueryException("sql函数创建发生异常");
        }
    }

    public GroupByFunction distinct() {
        this.distinct = true;
        return this;
    }
}
