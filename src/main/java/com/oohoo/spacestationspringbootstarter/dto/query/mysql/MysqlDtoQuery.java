package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractDtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.Query;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 14 November 2022
 */
public class MysqlDtoQuery extends AbstractDtoQuery {

    private MysqlDtoQuery(){}

    public static DtoQuery init(DTO dto) {
        MysqlDtoQuery mysqlDtoQuery = new MysqlDtoQuery(){};
        mysqlDtoQuery.dto = dto;
        mysqlDtoQuery.dtoClass = dto.getClass();
        mysqlDtoQuery.declaredFields = mysqlDtoQuery.dtoClass.getDeclaredFields();
        return mysqlDtoQuery;

    }
}
