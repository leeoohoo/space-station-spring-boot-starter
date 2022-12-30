package com.oohoo.spacestationspringbootstarter.dto.query;


import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Exclude;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.*;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
public abstract class AbstractSqlQuery implements FromManager, CdnManager, JoinManager, SelectManager,
        HavingManager, OrderByManager,UpdateManager,DeleteManager, Query {

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
        this.sqlContext.initSelect();
        return this;
    }



    /**
     * 获得Select 语句,如未调用该方法则默认查询DTO 中所有字段
     *
     * @return
     */
    @Override
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final <T> SelectManager select(SelectColumn<T, ?>... columns) {
        StringBuilder select = this.sqlContext.getSelect();
        Arrays.stream(columns).forEach(it -> {
            Column column = Column.create(it, "");
            select.append(column.getSelectFieldSql()).append(", ").append("\n");
            this.sqlContext.addAlias(column);
        });
        this.sqlContext.setSelectField();
        return this;
    }

    @Override
    public final <T> SelectManager select(SelectColumn<T, ?> selectColumn, String alias) {
        StringBuilder select = this.sqlContext.getSelect();
        Column column = Column.create(selectColumn, alias);
        select.append(column.getSelectFieldSql()).append(", ").append("\n");
        this.sqlContext.addAlias(column);
        this.sqlContext.setSelectField();
        return this;
    }

    /**
     * 根据传入的实体类或DTO生成 需要查询的字段
     *
     * @param clazz
     * @return
     */
    @Override
    public SelectManager select(@Nullable Class<?> clazz) {
        Assert.notNull(clazz, "传入要查询的实体类 类型不能为空");
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> null == it.getDeclaredAnnotation(Exclude.class)).collect(Collectors.toList());
        List<Column> columns = ClassUtils.fieldsToColumns(clazz, fields);
        StringBuilder select = this.sqlContext.getSelect();
        columns.forEach(it -> {
            select.append(it.getSelectFieldSql()).append(", ").append("\n");
            this.sqlContext.addAlias(it);
        });
        this.sqlContext.setSelectField();
        return this;
    }

    @Override
    public SelectManager select(@Nullable GeneralFunction... sqlFunction) {
        Assert.notNull(sqlFunction, "传入的sql 函数不能为空");
        StringBuilder select = this.sqlContext.getGeneralFunctionSql();
        Arrays.stream(sqlFunction).forEach(it -> {
            select.append(it.getFuncSql()).append(" as ").append(it.getAlias()).append(", ").append("\n");
            this.sqlContext.addAlias(it.getAlias());
        });
        this.sqlContext.setSelectField();
        return this;
    }

    @Override
    public SelectManager select(@Nullable GroupByFunction... sqlFunction) {
        Assert.notNull(sqlFunction, "传入的sql 函数不能为空");
        this.sqlContext.groupBy();
        StringBuilder select = this.sqlContext.getGroupFunctionSql();
        Arrays.stream(sqlFunction).forEach(it -> {
            select.append(it.getFuncSql()).append(" as ").append(it.getAlias()).append(", ").append("\n");
        });
        this.sqlContext.setSelectField();
        return this;
    }

    @Override
    public <T> UpdateManager update(Class<T> clazz) {
        this.sqlContext.setFrom(clazz);
        this.sqlContext.initUpdate();
        return this;
    }

    @Override
    public <T> UpdateManager set(SelectColumn<T,?> selectColumn, Object object) {
        this.addSetFiled(selectColumn,object);
        return this;
    }

    @Override
    public <T> DeleteManager delete(Class<T> clazz) {
        this.sqlContext.setFrom(clazz);
        this.sqlContext.initDelete();
        return this;
    }

    @Override
    public CdnManager where() {
        return this;
    }


    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.EQ);
        return this;
    }

    @Override
    public <T> CdnManager eq(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.EQ, required);
        return this;
    }


    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, LikeLocation.ALL);
        return this;
    }

    @Override
    public <T> CdnManager like(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required, LikeLocation.ALL);
        return this;
    }


    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, LikeLocation.LEFT);
        return this;
    }

    @Override
    public <T> CdnManager likeLeft(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required, LikeLocation.LEFT);
        return this;
    }


    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, LikeLocation.RIGHT);
        return this;
    }

    @Override
    public <T> CdnManager likeRight(SelectColumn<T, ?> column, String value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LIKE, required, LikeLocation.RIGHT);
        return this;
    }


    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.LE);
        return this;
    }

    @Override
    public <T> CdnManager le(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LE, required);
        return this;
    }


    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.LT);
        return this;
    }

    @Override
    public <T> CdnManager lt(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.LT, required);
        return this;
    }


    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.GE);
        return this;
    }

    @Override
    public <T> CdnManager ge(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.GE, required);
        return this;
    }


    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.GT);
        return this;
    }

    @Override
    public <T> CdnManager gt(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.GT, required);
        return this;
    }


    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value) {
        this.addCdnAndParams(column, value, OpEnum.IN);
        return this;
    }

    @Override
    public <T> CdnManager in(SelectColumn<T, ?> column, List<?> value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.IN, required);
        return this;
    }


    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value) {
        this.addCdnAndParams(column, value, OpEnum.NOT_IN);
        return this;
    }

    @Override
    public <T> CdnManager notIn(SelectColumn<T, ?> column, List<?> value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.NOT_IN, required);
        return this;
    }


    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value) {
        this.addCdnAndParams(column, value, OpEnum.NE);
        return this;
    }

    @Override
    public <T> CdnManager ne(SelectColumn<T, ?> column, Object value, boolean required) {
        this.addCdnAndParams(column, value, OpEnum.NE, required);
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
    public HavingManager having() {
        this.sqlContext.addBracket(this.sqlContext.getCdn());
        return this;
    }

    @Override
    public OrderByManager order() {
        this.sqlContext.setOrder(new StringBuilder(" order by "));
        return this;
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
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1, condition);
        return this;
    }

    @Override
    public <T, J> JoinManager on(SelectColumn<T, ?> column, String alias,
                                 OpEnum opEnum,
                                 SelectColumn<J, ?> column1, String alias1,
                                 CdnContainer... condition) {
        Column selectColumn = Column.create(column, alias);
        Column selectColumn1 = Column.create(column1, alias1);
        this.sqlContext.addOn(selectColumn, opEnum, selectColumn1, condition);
        return this;
    }

    private void addJoin(JoinEnum joinEnum, Class<?> clazz, String... alias) {
        String tableName = ClassUtils.getTableName(clazz);
        String tableAlias = tableName;
        if (null != alias && alias.length > 0) {
            if (alias[0].trim().contains(" ")) {
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
        this.sqlContext.addParams(value);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum, boolean required, LikeLocation likeLocation) {
        Column field = Column.create(selectColumn);
        if (required && null == value) {
            throw new DtoQueryException("参数不能为空，fieldName:[" + field.getField() + "]");
        }
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum, likeLocation);
        this.sqlContext.addCdn(cdnSql);
        this.sqlContext.addParams(value);
    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum) {
        Column field = Column.create(selectColumn);
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum);
        this.sqlContext.addCdn(cdnSql);
        this.sqlContext.addParams(value);

    }

    private <T> void addCdnAndParams(SelectColumn<T, ?> selectColumn, Object value, OpEnum opEnum, LikeLocation likeLocation) {
        Column field = Column.create(selectColumn);
        if (null == value) {
            return;
        }
        String cdnSql = field.getCdnSql(opEnum, likeLocation);
        this.sqlContext.addCdn(cdnSql);
        this.sqlContext.addParams(value);
    }

    private <T> void addSetFiled(SelectColumn<T,?> selectColumn, Object value) {
        Column column = Column.create(selectColumn);
        this.sqlContext.addSet(column.getCdnSql(OpEnum.EQ));
        this.sqlContext.addParams(value);
    }

    @Override
    public HavingManager havOr() {
        this.sqlContext.setLogicEnum(LogicEnum.OR);
        return this;
    }

    @Override
    public HavingManager havBracket() {
        this.sqlContext.setHavBracket();
        return this;
    }

    @Override
    public HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, Object value) {
        if (null == value) {
            return this;
        }
        this.sqlContext.addHaving(groupByFunction.getFuncSql() + opEnum.getOp() + "?");
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, Object value, Boolean required) {
        if (required) {
            if (null == value) {
                throw new DtoQueryException("生成条件搜索时发生错误，查询的参数不能为空");
            }
        } else {
            if (null == value) {
                return this;
            }
        }
        this.sqlContext.addHaving(groupByFunction.getFuncSql() + " " + opEnum.getOp() + " ? ");
        this.sqlContext.getGroupAlias().append(groupByFunction.getAlias()).append(",");
        this.sqlContext.addParams(value);
        return this;
    }

    @Override
    public <T> HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, SelectColumn<T, ?> column) {
        Column column1 = Column.create(column);
        String s = column1.getTableName() + "." + column1.getField();
        this.sqlContext.addHaving(groupByFunction.getFuncSql() + " " + opEnum.getOp() + " " + s);
        this.sqlContext.addAlias(column1);
        return this;
    }

    @Override
    public <T> HavingManager addCdn(GroupByFunction groupByFunction, OpEnum opEnum, GroupByFunction groupByFunction1) {
        this.sqlContext.addHaving(groupByFunction.getFuncSql() + " " + opEnum.getOp() + " " + groupByFunction1.getFuncSql());
        return this;
    }

    @Override
    public <T> OrderByManager by(SelectColumn<T, ?> column, OrderByEnum order) {
        Column column1 = Column.create(column);
        this.sqlContext.addOrderBy(column1.getTableName() + "." + column1.getField()+ " "+order.getSortType());
        return this;
    }

    @Override
    public <T> OrderByManager by(SelectColumn<T, ?> column) {
        Column column1 = Column.create(column);
        this.sqlContext.addOrderBy(column1.getTableName() + "." + column1.getField()+ " "+OrderByEnum.ASC.getSortType());
        return this;
    }

    @Override
    public <T> OrderByManager by(OrderByEnum order, SelectColumn<T, ?>... columns) {
        if(null == columns || columns.length <= 0) {
            throw new DtoQueryException("添加order by 发生异常, 请选择要排序的字段");
        }
        Arrays.stream(columns).forEach(it -> {
            this.by(it,order);
        });
        return this;
    }
}
