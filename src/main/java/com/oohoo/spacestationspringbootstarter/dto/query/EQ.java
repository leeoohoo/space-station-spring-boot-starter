package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.mysql.MysqlQuery;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 11 November 2022
 */
public class EQ {

    /**
     * 会根据传入的DtoClass 自动生成 select语句，但是如果写了select 字段则优先select方法中传递的字段
     * @param dtoClass
     * @return
     */
    public static AbstractSqlQuery find(){
        MysqlQuery mysqlQuery = new MysqlQuery();
        mysqlQuery.create();
        return mysqlQuery;
    }


    public static <T extends DtoQuery>  DtoQuery find(T dto) {
        return null;
    }
}
