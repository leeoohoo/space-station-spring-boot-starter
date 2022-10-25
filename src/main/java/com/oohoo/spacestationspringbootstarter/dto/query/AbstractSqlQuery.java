package com.oohoo.spacestationspringbootstarter.dto.query;


import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public abstract class AbstractSqlQuery implements CdnManager, JoinManager, SelectManager, Query {

    protected SqlContext sqlContext;

    protected List<Field> fields;
    protected Class<?> clazz = this.getClass();


    protected AbstractSqlQuery creat() {
        this.initContext();
        this.fields = Arrays.asList(this.clazz.getDeclaredFields());
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
            Column column = Column.create(it, "");
            select.append(column.getSelectFieldSql()).append(", ");
        });
        this.sqlContext.setSelect(select);
        return this;
    }

    @Override
    public final <T, D> SelectManager select(SelectColumn<T, ?> selectColumn, String alias) {
        StringBuilder select = this.sqlContext.getSelect();
        Column column = Column.create(selectColumn, alias);
        select.append(column.getSelectFieldSql()).append(", ");
        return this;
    }

    @Override
    public CdnManager where() {
        this.sqlContext.setCdn(new StringBuilder(" where "));
        return (CdnManager) this;
    }


    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.EQ));
        return this;
    }

    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.EQ, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column) {
        this.sqlContext.addParams("'%" + this.addCdnAndParams(column, OpEnum.LIKE) + "%'");
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required);
        this.sqlContext.addParams("'%" + value + "%'");
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column) {
        this.sqlContext.addParams("'%" + this.addCdnAndParams(column, OpEnum.LIKE) + "'");
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required);
        this.sqlContext.addParams("'%" + value + "'");
        return this;
    }

    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column) {
        this.sqlContext.addParams("'" + this.addCdnAndParams(column, OpEnum.LIKE) + "%'");
        return this;
    }

    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required);
        this.sqlContext.addParams("'" + value + "%'");
        return this;
    }

    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.LE));
        return this;
    }

    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.LE, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.LT));
        return this;
    }

    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.LT, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.GE));
        return this;
    }

    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.GE, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.GT));
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.GT, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.IN));
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.IN, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.NOT_IN));
        return this;
    }

    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.NOT_IN, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column) {
        this.sqlContext.addParams(this.addCdnAndParams(column, OpEnum.NE));
        return this;
    }

    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value, boolean... required) {
        this.addCdnAndParams(column, value, OpEnum.NE, required);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public CdnManager or() {
        this.sqlContext.setLogicEnum(LogicEnum.OR);
        return this;
    }

    @Override
    public CdnManager bracket() {
        this.sqlContext.setBracket();
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

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum, boolean... requireds) {
        Column field = Column.create(selectColumn);
        boolean required = false;
        if (requireds.length > 0) {
            required = requireds[0];
        }
        if (required && null == value) {
            throw new DtoQueryException("参数不能为空，fieldName:[" + field.getField() + "]");
        }
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum);
        this.sqlContext.addCdn(cdnSql);
    }

    /**
     * 添加where 条件与传入的参数
     *
     * @param selectColumn
     * @param opEnum
     * @param <T>
     * @return
     */
    private <T> Object addCdnAndParams(SelectColumn<T, ?> selectColumn, OpEnum opEnum) {
        Column field = Column.create(selectColumn);
        Object dtoFieldValue = getDtoFieldValue(field.getField());
        if (null == dtoFieldValue) {
            return dtoFieldValue;
        }
        String cdnSql = field.getCdnSql(opEnum);
        this.sqlContext.addCdn(cdnSql);
        return dtoFieldValue;
    }

    /**
     * 获取参数
     * 判断参数是否必填
     *
     * @param fieldName
     * @return
     */
    private Object getDtoFieldValue(String fieldName) {
        Optional<Field> first = fields.stream().filter(it -> it.getName().equals(fieldName)).findFirst();
        if (!first.isPresent()) {
            return null;
        }
        try {
            Field field = first.get();
            Object result = field.get(this);
            Optional<com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition> condition =
                    Arrays.stream(field.getDeclaredAnnotations()).map(it -> it.annotationType().getDeclaredAnnotation(
                            com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition.class)).findFirst();
            boolean conditionRequired = condition.isPresent() && condition.get().required();
            if (conditionRequired && null == result) {
                throw new DtoQueryException("参数不能为空，fieldName:[" + field.getName() + "]");
            }
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 获取参数
     *
     * @param fieldName
     * @return
     */
    private Object getDtoFieldValue(String fieldName, Boolean required) {
        Optional<Field> first = fields.stream().filter(it -> it.getName().equals(fieldName)).findFirst();
        if (!first.isPresent()) {
            return null;
        }
        try {
            Field field = first.get();
            Object result = field.get(this);
            if ((null != required && required) && null == result) {
                throw new DtoQueryException("参数不能为空，fieldName:[" + field.getName() + "]");
            }
            return result;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
