package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.SqlFunctionEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import lombok.Data;
import org.springframework.util.CollectionUtils;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
@Data
public  class SqlFunction implements CdnSqlFunction, DateSqlFunction, MathSqlFunction,StringSqlFunction {

    private SqlFunctionEnum sqlFunctionEnum;
    private String alias;

    private String funcSql;
    private SqlFunction(){};


    @Override
    public <T> MathSqlFunction abs(SelectColumn<T, ?> selectColumn, SelectColumn<T, ?>... alias) {
        Column column = Column.create(selectColumn);
        String defaultAlias =  column.getAlias() + "Abs";
        SelectColumn<T,?> defult = null;
        if(null != alias && alias.length > 0) {
            defult = alias[0];
        }
        if(null != defult) {
            defaultAlias = Column.create(defult).getAlias();
        }
        String value = SqlFunctionEnum.ABS.getValue();
        String replace = value.replace("?", column.getTableName() + "." + column.getField());

        return null;
    }

    @Override
    public GeneralFunction create(SqlFunctionEnum sqlFunctionEnum, String funcSql, String alias) {
        return new SqlFunction();
    }
}
