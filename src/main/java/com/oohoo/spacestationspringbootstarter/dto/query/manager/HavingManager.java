package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 28 November 2022
 */
public interface HavingManager extends SqlManager {


    OrderByManager order();
    HavingManager havOr();

    HavingManager havBracket();

    HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, Object value);

    HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, Object value, Boolean required);

    <T> HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, SelectColumn<T,?> column);



}
