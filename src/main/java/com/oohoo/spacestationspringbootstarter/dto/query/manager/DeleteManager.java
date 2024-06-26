package com.oohoo.spacestationspringbootstarter.dto.query.manager;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 29 十二月 2022
 */
public interface DeleteManager extends SqlManager {

    <T> DeleteManager delete(Class<T> clazz);

    CdnManager where();
}
