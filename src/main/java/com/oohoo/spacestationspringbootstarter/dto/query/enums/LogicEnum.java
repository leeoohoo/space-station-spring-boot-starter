package com.oohoo.spacestationspringbootstarter.dto.query.enums;

public enum LogicEnum {
    OR(" or "),
    AND(" and ");

    String value;

    LogicEnum(String value) {
        this.value = value;
    }
}
