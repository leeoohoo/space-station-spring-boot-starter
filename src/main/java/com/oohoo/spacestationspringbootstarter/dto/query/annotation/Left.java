package com.oohoo.spacestationspringbootstarter.dto.query.annotation;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
public @interface Left {
    Class<?> value();

    String fromField();

    Class<?> leftField();

    OpEnum op();
}
