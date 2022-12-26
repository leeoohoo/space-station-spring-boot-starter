package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.lambda.BatchInsertContainer;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 11 November 2022
 */
public interface DtoInserter {
    void scan();


    String getInsertOneSql();

    Object getEntity();

    List<Object> getParams();

    void buildInsert();

    void buildValues(DTO dto);

    void addParam(Field field);

    void build();

    void buildBatch();

    List<BatchInsertContainer> getBatchInsertContainers();
}
