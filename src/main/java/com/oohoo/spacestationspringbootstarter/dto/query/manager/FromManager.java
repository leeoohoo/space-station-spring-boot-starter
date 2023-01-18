package com.oohoo.spacestationspringbootstarter.dto.query.manager;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 28 October 2022
 */
public interface FromManager extends SqlManager {


    /**
     * 指定要查询的表
     * @param clazz
     * @return
     */
    SelectManager from(Class<?> clazz);



}
