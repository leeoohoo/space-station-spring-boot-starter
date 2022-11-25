package com.oohoo.spacestationspringbootstarter.dto.query;


import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Exclude;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LikeLocation;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.CdnManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.FromManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.JoinManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SelectManager;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public abstract  class AbstractSqlQuery implements FromManager, CdnManager, JoinManager, SelectManager, Query {

    protected SqlContext sqlContext;

    protected List<Field> fields;

    protected Class<?> dtoClazz;


    public AbstractSqlQuery create() {
        this.initContext();
        this.dtoClazz = this.getClass();
        this.fields = Arrays.asList(this.dtoClazz.getDeclaredFields());
        return this;
    }






    @Override
    public final AbstractSqlQuery from(Class<?> clazz) {
        this.sqlContext.setFrom(clazz);
        return this;
    }

    /**
     * 获得Select 语句,如未调用该方法则默认查询DTO 中所有字段
     *
     * @return
     */
    @Override
    @SafeVarargs
    public final <T>  SelectManager select(SelectColumn<T, ?>... columns) {
        StringBuilder select = this.sqlContext.getSelect();
        Arrays.stream(columns).forEach(it -> {
            Column column = Column.create(it, "");
            select.append(column.getSelectFieldSql()).append(", ").append("\n");
            this.sqlContext.getAlias().append(column.getAlias()).append(", ");
        });
        return this;
    }

    @Override
    public final <T> SelectManager select(SelectColumn<T, ?> selectColumn, String alias) {
        StringBuilder select = this.sqlContext.getSelect();
        Column column = Column.create(selectColumn, alias);
        select.append(column.getSelectFieldSql()).append(", ").append("\n");
        this.sqlContext.getAlias().append(alias).append(", ");
        return this;
    }

    @Override
    public SelectManager select(@Nullable Class<?> clazz) {
        Assert.notNull(clazz,"传入要查询的DTO 类型不能为空");
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> null == it.getDeclaredAnnotation(Exclude.class)).collect(Collectors.toList());
        List<Column> columns = ClassUtils.fieldsToColumns(clazz, fields);
        StringBuilder select = this.sqlContext.getSelect();
        columns.forEach(it -> {
            select.append(it.getSelectFieldSql()).append(", ").append("\n");
            this.sqlContext.getAlias().append(it.getAlias()).append(", ");
        });
        return this;
    }

    @Override
    public SelectManager select(@Nullable GeneralFunction... sqlFunction) {
        Assert.notNull(sqlFunction,"传入的sql 函数不能为空");
        StringBuilder select = this.sqlContext.getGeneralFunctionSql();
        Arrays.stream(sqlFunction).forEach(it -> {
            select.append(it.getFuncSql()).append(" as ").append(it.getAlias()).append(", ").append("\n");
            this.sqlContext.getAlias().append(it.getAlias()).append(", ");
        });
        return this;
    }

    @Override
    public SelectManager select(@Nullable GroupByFunction... sqlFunction) {
        Assert.notNull(sqlFunction, "传入的sql 函数不能为空");
        this.sqlContext.groupBy();
        StringBuilder select = this.sqlContext.getGroupFunctionSql();
        Arrays.stream(sqlFunction).forEach(it -> {
            select.append(it.getFuncSql()).append(" as ").append(it.getAlias()).append(", ").append("\n");
            this.sqlContext.getGroupAlias().append(it.getAlias()).append(", ");
        });
        return this;
    }

    @Override
    public CdnManager where() {
        this.sqlContext.setCdn(new StringBuilder());
        return this;
    }




    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.EQ);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.EQ, required);
        this.sqlContext.addParams(value);
        return this;
    }



    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE,LikeLocation.ALL);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required,LikeLocation.ALL);
        this.sqlContext.addParams( value);
        return this;
    }



    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE,LikeLocation.LEFT);
        this.sqlContext.addParams(value );
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required,LikeLocation.LEFT);
        this.sqlContext.addParams(value );
        return this;
    }



    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE,LikeLocation.RIGHT);
        this.sqlContext.addParams( value );
        return this;
    }
    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required, LikeLocation.RIGHT);
        this.sqlContext.addParams(value);
        return this;
    }




    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.LE);
        this.sqlContext.addParams(value);
        return this;
    }
    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LE, required);
        this.sqlContext.addParams(value);
        return this;
    }




    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.LT);
        this.sqlContext.addParams(value);
        return this;
    }
    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LT, required);
        this.sqlContext.addParams(value);
        return this;
    }




    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.GE);
        this.sqlContext.addParams(value);
        return this;
    }
    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.GE, required);
        this.sqlContext.addParams(value);
        return this;
    }


    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.GT);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.GT, required);
        this.sqlContext.addParams(value);
        return this;
    }



    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value) {
        this.addCdnAndParams(column, value, OpEnum.IN);
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.IN, required);
        this.sqlContext.addParams(value);
        return this;
    }



    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value) {
        this.addCdnAndParams(column, value, OpEnum.NOT_IN);
        this.sqlContext.addParams(value);
        return this;
    }
    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.NOT_IN, required);
        this.sqlContext.addParams(value);
        return this;
    }



    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.NE);
        this.sqlContext.addParams(value);
        return this;
    }
    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value, boolean required) {
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
    public void findOne() {


    }

    @Override
    public JoinManager inner(Class<?> clazz, String... alias) {
        this.addJoin(JoinEnum.INNER, clazz, alias);
        return this;
    }

    @Override
    public JoinManager left(Class<?> clazz, String... alias) {
        this.addJoin(JoinEnum.LEFT, clazz, alias);
        return this;
    }

    @Override
    public JoinManager right(Class<?> clazz, String... alias) {
        this.addJoin(JoinEnum.RIGHT, clazz, alias);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1) {
        Column selectColumn = Column.create(column);
        Column selectColumn1 = Column.create(column1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1) {
        Column selectColumn = Column.create(column, alias);
        Column selectColumn1 = Column.create(column1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1, String alias1) {
        Column selectColumn = Column.create(column, alias);
        Column selectColumn1 = Column.create(column1, alias1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1, String alias1) {
        Column selectColumn = Column.create(column);
        Column selectColumn1 = Column.create(column1, alias1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1,
                                 CdnContainer... condition) {
        Column selectColumn = Column.create(column);
        Column selectColumn1 = Column.create(column1);
        this.sqlContext.addOn(selectColumn,opEnum,selectColumn1,condition);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1, String alias1,
                                 CdnContainer... condition) {
        Column selectColumn = Column.create(column,alias);
        Column selectColumn1 = Column.create(column1, alias1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1,condition);
        return this;
    }

    private void addJoin(JoinEnum joinEnum, Class<?> clazz, String... alias) {
        String tableName = ClassUtils.getTableName(clazz);
        String tableAlias = tableName;
        if (null != alias && alias.length > 0) {
            if(alias[0].trim().contains(" ")) {
                throw new DtoQueryException("别名不允许有空格");
            }
            tableAlias = alias[0].trim();
        }
        this.sqlContext.addJoin(joinEnum, tableName, tableAlias);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum, boolean required) {
        Column field = Column.create(selectColumn);
        if (required && null == value) {
            throw new DtoQueryException("参数不能为空，fieldName:[" + field.getField() + "]");
        }
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum);
        this.sqlContext.addCdn(cdnSql);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum, boolean required,LikeLocation likeLocation) {
        Column field = Column.create(selectColumn);
        if (required && null == value) {
            throw new DtoQueryException("参数不能为空，fieldName:[" + field.getField() + "]");
        }
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum,likeLocation);
        this.sqlContext.addCdn(cdnSql);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum) {
        Column field = Column.create(selectColumn);
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum);
        this.sqlContext.addCdn(cdnSql);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum,LikeLocation likeLocation) {
        Column field = Column.create(selectColumn);
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum,likeLocation);
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
        if(!this.getClass().equals(this.dtoClazz)) {
          return null;
        }
        try {
            Field field = first.get();
            field.setAccessible(true);
            return field.get(this);
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
