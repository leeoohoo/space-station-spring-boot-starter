package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
public interface Butler {


    <T> EPage<T> findPage(DtoQuery dtoQuery, Class<T> resultClazz, Integer pageNo, Integer pageSize);

    <T> EPage<T> findPage(SqlManager sqlManager, Class<T> resultClazz, Integer pageNo, Integer pageSize);

    <T> List<T> findList(DtoQuery dtoQuery, Class<T> resultClazz);

    <T> List<T> findList(SqlManager sqlManager, Class<T> resultClazz);

    <T> T findOne(DtoQuery dtoQuery, Class<T> resultClazz);

    <T> T findOne(SqlManager sqlManager, Class<T> resultClazz);

    <T> EPage<T> count(String sql,List<Object> params);

    Object insert(DTO dto);

    Boolean insertBatch(List<DTO> dtoList, Integer batchSize);

    Boolean update(DtoQuery dtoQuery);


    Boolean update(SqlManager sqlManager);


    Boolean delete(DtoQuery dtoQuery);


    Boolean delete(SqlManager sqlManager);


}
