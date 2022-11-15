package com.oohoo.spacestationspringbootstarter.dto.query.manager;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 15 November 2022
 */
public interface DtoSqlManager {

    void build();

    void selectBuild();

    void cdnBuild();

    void joinBuild();

}
