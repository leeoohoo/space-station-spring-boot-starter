package com.oohoo.spacestationspringbootstarter.dto.query.enums;

public enum JoinEnum {
    LEFT(" left "),
    RIGHT(" right "),
    INNER(" inner ");

    final String type;

    public String getType() {
        return this.type;
    }
    JoinEnum(String type) {
        this.type = type;
    }
}
