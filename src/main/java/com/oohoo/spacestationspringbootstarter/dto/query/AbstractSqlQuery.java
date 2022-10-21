package com.oohoo.spacestationspringbootstarter.dto.query;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.SerializedLambda;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public abstract class AbstractSqlQuery implements CdnManager, JoinManager, SelectManager, Query {

    protected SqlContext sqlContext;
    protected Class<?> clazz = this.getClass();


    protected AbstractSqlQuery creat() {
        this.initContext();
        return this;
    }

    public AbstractSqlQuery from(Class<?> clazz) {
        return this;
    }

    /**
     * 获得Select 语句,如未调用该方法则默认查询DTO 中所有字段
     *
     * @return
     */
    @Override
    @SafeVarargs
    public final <T> SelectManager select(SelectColumn<T, ?>... columns) {
        StringBuilder select = this.sqlContext.getSelect();
        Arrays.stream(columns).forEach(it -> {
            SerializedLambda resolve = SerializedLambda.resolve(it);
        });

        return this;
    }
    @Override
    public final <T, D> SelectManager select(SelectColumn<T, ?> column, SelectColumn<D, ?> alias) {
        return this;
    }

    @Override
    public CdnManager where() {
        return (CdnManager) this;
    }


    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value, boolean... required) {
        return this;
    }

    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column) {
        return this;
    }

    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value, boolean... required) {
        return this;
    }

    @Override
    public CdnManager or() {
        return this;
    }

    @Override
    public CdnManager bracket() {
        return this;
    }

    @Override
    public JoinManager inner(Class<?> clazz) {
        return this;
    }

    @Override
    public JoinManager left(Class<?> clazz) {
        return this;
    }

    @Override
    public JoinManager right(Class<?> clazz) {
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1) {
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, OpEnum opEnum, SelectColumn<J, ?> column1, Condition... condition) {
        return this;
    }
}
