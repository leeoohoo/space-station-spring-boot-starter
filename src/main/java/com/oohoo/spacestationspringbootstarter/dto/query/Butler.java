package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
public interface Butler {


    <T> EPage<T> findPage(DtoQuery dtoQuery, Class<T> resultClazz, Integer pageNo, int pageSize);

    <T> EPage<T> findPage(SqlManager sqlManager, Class<T> resultClazz, Integer pageNo, int pageSize);

    <T> List<T> findList(DtoQuery dtoQuery, Class<T> resultClazz);

    <T> List<T> findList(SqlManager sqlManager, Class<T> resultClazz);

    <T> T findOne(DtoQuery dtoQuery, Class<T> resultClazz);

    <T> T findOne(SqlManager sqlManager, Class<T> resultClazz);


}
