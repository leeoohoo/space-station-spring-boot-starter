package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractDtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoQuery;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.JoinColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.JoinContainer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 14 November 2022
 */
public class MysqlDtoQuery extends AbstractDtoQuery {

    private MysqlDtoQuery(){}

    public static DtoQuery init(DTO dto) {
        MysqlDtoQuery mysqlDtoQuery = new MysqlDtoQuery(){};
        mysqlDtoQuery.dto = dto;
        mysqlDtoQuery.dtoClass = dto.getClass();
        mysqlDtoQuery.declaredFields = mysqlDtoQuery.dtoClass.getDeclaredFields();
        return mysqlDtoQuery;
    }

    @Override
    public void selectBuild() {
        if(CollectionUtils.isEmpty(this.columns)) {
            throw new DtoQueryException("[缺少查询的字段，请检查DTO: "+this.dtoClass.getName()+"]");
        }
        this.selectSql.append("select \n");
        this.columns.forEach(it -> {
            this.selectSql.append(it.getTableName()).append(".")
                    .append(it.getField()).append(" as ").append(it.getAlias()).append(",\n");

        });
        this.selectSql = new StringBuilder(this.selectSql.substring(0,this.selectSql.lastIndexOf(","))).append("\n");
        this.selectSql.append(" from ").append(ClassUtils.getTableName(this.fromClass)).append("\n");
    }

    @Override
    public void cdnBuild() {
        this.cdnSql.append(" where ");
        AtomicBoolean ifBegin = new AtomicBoolean(true);
        this.cdnContainers.stream().sorted(Comparator.comparing(CdnContainer::getOrder)).forEach(it -> {
            if(!ifBegin.get()) {
                this.cdnSql.append(it.getLogicSymbol().getValue());
            }
            Column column = it.getColumn();
            JoinColumn joinColumn = it.getField().getDeclaredAnnotation(JoinColumn.class);
            if(null != joinColumn) {
                column.setTableName(ClassUtils.getTableName(joinColumn.joinClass()));
                column.setField(ClassUtils.camelToUnderline(joinColumn.columnName()));
                column.setAlias(it.getField().getName());
            }
            //如果是Like
            if(null != it.getLikeLocation()) {
                this.cdnSql.append(column.getCdnSql(it.getOpEnum(),it.getLikeLocation())).append(" \n");
            }else {
                this.cdnSql.append(column.getCdnSql(it.getOpEnum())).append(" \n");
            }
            this.params.add(it.getValue());
            ifBegin.set(false);
        });
    }

    @Override
    public void joinBuild() {
        this.joinContainers.stream().sorted(Comparator.comparing(JoinContainer::getOrder).reversed()).forEach(it -> {
            this.joinSql.append(it.getJoinEnum().getType())
                    .append(ClassUtils.getTableName(it.getJoinClass())).append(" as ")
                    .append(ClassUtils.getTableName(it.getJoinClass()))
                    .append(" on ")
                    .append(ClassUtils.getTableName(it.getFromClass())).append(".").append(it.getFromField())
                    .append(it.getOpEnum().getOp())
                    .append(ClassUtils.getTableName(it.getJoinClass())).append(".").append(it.getJoinField())
                    .append(" \n");
        });
    }
}
