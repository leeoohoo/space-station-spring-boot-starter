package com.oohoo.spacestationspringbootstarter.dto.query.enums;


public enum BracketEnum {
    BEGEN(" ("),
    END(") ");

    final String type;
    public String getType() {
        return this.type;
    }
    BracketEnum(String type) {
        this.type = type;
    }
}
