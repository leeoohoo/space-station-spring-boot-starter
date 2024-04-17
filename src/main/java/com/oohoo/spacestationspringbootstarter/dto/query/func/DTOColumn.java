package com.oohoo.spacestationspringbootstarter.dto.query.func;

import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/10/19
 */
@FunctionalInterface
public interface DTOColumn<T extends DTO, R> extends Function<T, R>, Serializable {

    static <T extends DTO, R> String getFieldName(DTOColumn<T,R> dtoColumn) {
        SerializedLambda resolve = SerializedLambda.resolve(dtoColumn);
        String implMethodName = resolve.getImplMethodName();
        return ClassUtils.getFiledName(implMethodName);
    }
}
