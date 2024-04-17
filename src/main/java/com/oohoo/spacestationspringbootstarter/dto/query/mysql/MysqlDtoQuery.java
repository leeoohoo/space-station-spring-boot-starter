package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractDtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.ClazzIsNull;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.JoinColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.*;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 14 November 2022
 */
public class MysqlDtoQuery extends AbstractDtoQuery {

    private MysqlDtoQuery() {
    }

    public static DtoQuery init(DTO dto) {
        MysqlDtoQuery mysqlDtoQuery = new MysqlDtoQuery();
        mysqlDtoQuery.dto = dto;
        mysqlDtoQuery.dtoClass = dto.getClass();
        mysqlDtoQuery.declaredFields = mysqlDtoQuery.dtoClass.getDeclaredFields();
        Class<?> superclass = mysqlDtoQuery.dtoClass.getSuperclass();
        if (null != superclass) {
            mysqlDtoQuery.superFields = superclass.getDeclaredFields();
        }
        return mysqlDtoQuery;
    }

    @Override
    public void selectBuild() {
        if (CollectionUtils.isEmpty(this.columns)) {
            throw new DtoQueryException("[缺少查询的字段，请检查DTO: " + this.dtoClass.getName() + "]");
        }
        this.selectSql.append("select \n");
        this.columns.forEach(it -> {
            this.selectSql.append(it.getTableName()).append(".")
                    .append(it.getField()).append(" as ").append(it.getAlias()).append(",\n");

        });
        this.selectSql = new StringBuilder(this.selectSql.substring(0, this.selectSql.lastIndexOf(","))).append("\n");
        this.selectSql.append(" from ").append(ClassUtils.getTableName(this.fromClass)).append("\n");
    }

    @Override
    public void cdnBuild() {
        if (CollectionUtils.isEmpty(this.cdnContainers)) {
            return;
        }
        this.cdnSql.append(" where ");
        AtomicBoolean ifBegin = new AtomicBoolean(true);
        this.cdnContainers.stream().sorted(Comparator.comparing(CdnContainer::getOrder)).forEach(it -> {
            if (!ifBegin.get()) {
                this.cdnSql.append(it.getLogicSymbol().getValue());
            }
            Column column = it.getColumn();
            JoinColumn joinColumn = it.getField().getDeclaredAnnotation(JoinColumn.class);
            if (null != joinColumn) {
                column.setTableName(ClassUtils.getTableName(joinColumn.joinClass()));
                column.setField(ClassUtils.camelToUnderline(validateFieldName(joinColumn.columnName())));
                column.setAlias(it.getField().getName());
            }
            //如果是Like
            if (null != it.getLikeLocation()) {
                this.cdnSql.append(column.getCdnSql(it.getOpEnum(), it.getLikeLocation(), it.getTable(), it.getKey())).append(" \n");
            } else {
                this.cdnSql.append(column.getCdnSql(it.getOpEnum(), it.getTable(), it.getKey())).append(" \n");
            }
            this.params.add(it.getValue());
            ifBegin.set(false);
        });
    }

    @Override
    public void joinBuild() {
        if (CollectionUtils.isEmpty(this.joinContainers)) {
            return;
        }
        this.joinContainers.stream().sorted(Comparator.comparing(JoinContainer::getOrder).reversed()).forEach(it -> {
            this.joinSql.append(it.getJoinEnum().getType())
                    .append(ClassUtils.getTableName(it.getJoinClass())).append(" as ")
                    .append(ClassUtils.getTableName(it.getJoinClass()))
                    .append(" on ")
                    .append(ClassUtils.getTableName(it.getFromClass())).append(".").append(validateFieldName(it.getFromField()))
                    .append(it.getOpEnum().getOp())
                    .append(ClassUtils.getTableName(it.getJoinClass())).append(".").append(validateFieldName(it.getJoinField()))
                    .append(" \n");
        });
    }

    @Override
    public void orderBuild() {
        if (CollectionUtils.isEmpty(this.orderByContainers)) {
            return;
        }
        this.orderBySql.append(" order by ");

        this.orderByContainers.stream().sorted(Comparator.comparing(OrderByContainer::getOrder).reversed()).forEach(it -> {
            String tableName = ClassUtils.getTableName(this.fromClass);
            if (!it.getClazz().isAssignableFrom(ClazzIsNull.class)) {
                tableName = ClassUtils.getTableName(it.getClazz());
            }
            this.orderBySql.append(tableName)
                    .append(".")
                    .append(validateFieldName(ClassUtils.camelToUnderline(it.getFieldName())))
                    .append(" ")
                    .append(it.getOrderType().getSortType())
                    .append(", ");
        });

    }


    private String validateFieldName(String fieldName) {
        if (fieldName.contains(" ")) {
            throw new DtoQueryException("字段生成发生异常, 请检查字段名不能有空格");
        }
        return fieldName;
    }
}
