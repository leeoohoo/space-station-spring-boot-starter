package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/23
 */
public interface UpdateManager extends SqlManager {

    <T> UpdateManager update(Class<T> clazz);

    <T> UpdateManager set(SelectColumn<T, ?> selectColumn, Object object);

    CdnManager where();
}
