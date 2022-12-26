package com.oohoo.spacestationspringbootstarter.dto.query.intercept;

import java.util.Date;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/15
 */
public interface InsertOrUpdateIntercept {
    Object current();

    default Date time() {
        return new Date();
    }
}
