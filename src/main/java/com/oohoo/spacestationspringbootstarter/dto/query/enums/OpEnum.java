package com.oohoo.spacestationspringbootstarter.dto.query.enums;

public enum OpEnum {
    EQ(" = "),
    GT(" > "),
    LIKE(" like "),
    GE(" >= "),
    LT(" < "),
    LE(" <= "),
    IN(" in "),
    NOT_IN(" not in");

    final String op;
    public String getOp() {
        return this.op;
    }
    OpEnum(String op){
        this.op = op;
    }
}
