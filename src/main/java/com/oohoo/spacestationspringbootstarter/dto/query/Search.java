package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 01 December 2022
 */
public interface Search {

    <T> T findOne(DtoQuery dtoQuery,Class<T> resultClazz);

    <T> T findOne(SqlManager sqlManager,Class<T> resultClazz);
}
