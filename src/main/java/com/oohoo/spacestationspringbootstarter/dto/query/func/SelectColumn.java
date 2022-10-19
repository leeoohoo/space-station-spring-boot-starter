package com.oohoo.spacestationspringbootstarter.dto.query.func;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/10/19
 */
@FunctionalInterface
public interface SelectColumn<T, R> extends Function<T, R>, Serializable {
}
