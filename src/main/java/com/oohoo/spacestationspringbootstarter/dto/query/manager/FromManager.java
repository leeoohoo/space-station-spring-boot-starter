package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 28 October 2022
 */
public interface FromManager {


    /**
     * 指定要查询的表
     * @param clazz
     * @return
     */
    SelectManager from(Class<?> clazz);



}
