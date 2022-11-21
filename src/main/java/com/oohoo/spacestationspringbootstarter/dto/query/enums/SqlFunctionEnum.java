package com.oohoo.spacestationspringbootstarter.dto.query.enums;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
public enum SqlFunctionEnum {
    ABS(" abs(?) ");

    private String value;

    SqlFunctionEnum(String s) {
        this.value = s;
    }

    public String getValue(){
        return this.value;
    }
}
