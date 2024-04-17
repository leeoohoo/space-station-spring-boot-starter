package com.oohoo.spacestationspringbootstarter.dto.query;

import java.util.List;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 11 November 2022
 */
public interface DtoQuery {

    void scan();

    String getSql();

    List<Object> getParams();

    void build();

    void selectBuild();

    void cdnBuild();

    void joinBuild();

    void orderBuild();
}
