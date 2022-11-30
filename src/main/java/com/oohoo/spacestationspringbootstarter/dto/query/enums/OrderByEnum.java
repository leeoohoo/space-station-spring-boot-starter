package com.oohoo.spacestationspringbootstarter.dto.query.enums;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 30 November 2022
 */
public enum OrderByEnum {
    DESC(" desc "),
    ASC(" asc ");
    final String sortType;

    public String getSortType() {
        return this.sortType;
    }

    OrderByEnum(String sortType) {
        this.sortType = sortType;
    }
}
