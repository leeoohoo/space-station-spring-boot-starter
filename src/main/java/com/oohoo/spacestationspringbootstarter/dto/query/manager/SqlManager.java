package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import java.util.List;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 21 October 2022
 */
public interface SqlManager {
    String getSelectSql();

    String getUpdateSql();

    String getDeleteSql();

    /**
     * 获取参数
     *
     * @return
     */
    List<Object> getParams();

    SqlManager finish();
}
