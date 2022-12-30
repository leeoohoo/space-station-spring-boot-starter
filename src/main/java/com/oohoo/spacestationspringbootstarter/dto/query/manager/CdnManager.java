package com.oohoo.spacestationspringbootstarter.dto.query.manager;

import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public interface CdnManager extends SqlManager {



    <T> CdnManager eq(SelectColumn<T, ?> column, Object value);

    <T> CdnManager eq(SelectColumn<T, ?> column, Object value, boolean required);



    <T> CdnManager like(SelectColumn<T, ?> column, String value, boolean required);

    <T> CdnManager like(SelectColumn<T, ?> column, String value);



    <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value);

    <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value, boolean required);



    <T> CdnManager likeRight(SelectColumn<T, ?> column, String value);

    <T> CdnManager likeRight(SelectColumn<T, ?> column, String value, boolean required);



    <T> CdnManager le(SelectColumn<T, ?> column, Object value);

    <T> CdnManager le(SelectColumn<T, ?> column, Object value, boolean required);



    <T> CdnManager lt(SelectColumn<T, ?> column, Object value);

    <T> CdnManager lt(SelectColumn<T, ?> column, Object value, boolean required);



    <T> CdnManager ge(SelectColumn<T, ?> column, Object value);

    <T> CdnManager ge(SelectColumn<T, ?> column, Object value, boolean required);



    <T> CdnManager gt(SelectColumn<T, ?> column, Object value);

    <T> CdnManager gt(SelectColumn<T, ?> column, Object value, boolean required);



    <T> CdnManager in(SelectColumn<T, ?> column, List<?> value);

    <T> CdnManager in(SelectColumn<T, ?> column, List<?> value, boolean required);



    <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value);

    <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value, boolean required);



    <T> CdnManager ne(SelectColumn<T, ?> column, Object value);

    <T> CdnManager ne(SelectColumn<T, ?> column, Object value, boolean required);

    CdnManager or();

    CdnManager bracket();


    HavingManager having();

    OrderByManager order();



}
