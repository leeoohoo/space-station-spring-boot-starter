package com.oohoo.spacestationspringbootstarter.dto.query.manager;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 29 εδΊζ 2022
 */
public interface DeleteManager extends SqlManager{

    <T> DeleteManager delete(Class<T> clazz);

    CdnManager where();
}
