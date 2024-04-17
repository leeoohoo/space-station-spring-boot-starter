package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import lombok.Data;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 14 November 2022
 */
@Data
public class JoinContainer {

    private JoinEnum joinEnum;

    private Class<?> fromClass;

    private String fromField;

    private OpEnum opEnum;

    private Class<?> joinClass;

    private String joinField;

    private Integer order;


    private JoinContainer(){}

    public static JoinContainer create(Join join) {
        JoinContainer joinContainer = new JoinContainer();
        joinContainer.joinClass = join.joinClazz();
        joinContainer.joinField = ClassUtils.camelToUnderline(join.joinField());
        joinContainer.joinEnum = join.join();
        joinContainer.fromClass = join.fromClazz();
        joinContainer.fromField = ClassUtils.camelToUnderline(join.fromField());
        joinContainer.order = join.order();
        joinContainer.opEnum = join.op();
        return joinContainer;
    }


}
