package com.oohoo.spacestationspringbootstarter.dto.query;

import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 11 November 2022
 */
public abstract class AbstractDtoQuery implements DtoQuery {
    @Override
    public String getSql() {
        return null;
    }

    @Override
    public List<Object> getParams() {
        return null;
    }
}
