package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import lombok.Data;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 14 November 2022
 */
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
        joinContainer.joinField = join.joinField();
        joinContainer.joinEnum = join.join();
        joinContainer.fromClass = join.fromClazz();
        joinContainer.fromField = join.fromField();
        joinContainer.order = join.order();
        joinContainer.opEnum = join.op();
        return joinContainer;
    }

}
