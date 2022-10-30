package com.oohoo.spacestationspringbootstarter.dto.query;


import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface Query {
    void initContext();


    Query create();

    String getSql();

    List<Object> getParams();

}
