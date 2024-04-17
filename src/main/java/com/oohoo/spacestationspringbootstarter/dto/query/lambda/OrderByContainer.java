package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.OrderBy;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OrderByEnum;
import lombok.Data;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 30 November 2022
 */
@Data
public class OrderByContainer {

    private Class<?> clazz;

    private String fieldName;

    private OrderByEnum orderType;

    private Integer order;

    private OrderByContainer(){}

    public static OrderByContainer create(OrderBy order) {
        OrderByContainer orderByContainer = new OrderByContainer();
        orderByContainer.setOrderType(order.orderType());
        orderByContainer.setClazz(order.table());
        orderByContainer.setFieldName(order.field());
        orderByContainer.setOrder(order.order());
        return orderByContainer;
    }


}
