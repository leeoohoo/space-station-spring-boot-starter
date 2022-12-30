package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
public interface Butler {


    <T extends DTO> EPage<T> findPage(T dto, Integer pageNo, Integer pageSize);

    <T extends DTO> EPage<T> findPage(SqlManager sqlManager, Class<T> resultClazz, Integer pageNo, Integer pageSize);

    <T extends DTO> List<T> findList(T dto);

    <T> List<T> findList(SqlManager sqlManager, Class<T> resultClazz);

    <T extends DTO> T findOne(T dto);

    <T> T findOne(SqlManager sqlManager, Class<T> resultClazz);

    <T> EPage<T> count(String sql,List<Object> params);

    Object insert(DTO dto);

    <T extends DTO> void insertBatch(List<T> dtoList, Integer batchSize);




    void update(SqlManager sqlManager);




    void delete(SqlManager sqlManager);


}
