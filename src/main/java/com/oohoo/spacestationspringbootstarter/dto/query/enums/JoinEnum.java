package com.oohoo.spacestationspringbootstarter.dto.query.enums;

public enum JoinEnum {
    LEFT(" left join "),
    RIGHT(" right join "),
    INNER(" inner join ");

    final String type;

    public String getType() {
        return this.type;
    }
    JoinEnum(String type) {
        this.type = type;
    }
}
