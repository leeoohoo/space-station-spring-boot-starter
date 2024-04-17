package com.oohoo.spacestationspringbootstarter.dto.query.mysql;

import com.oohoo.spacestationspringbootstarter.dto.query.AbstractDtoInserter;
import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.DtoInserter;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.init.InsertInit;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/12/14
 */
public class MysqlDtoInserter extends AbstractDtoInserter {

    public static DtoInserter init(DTO dto) {
        MysqlDtoInserter mysqlDtoInserter = new MysqlDtoInserter();
        mysqlDtoInserter.dto = dto;
        mysqlDtoInserter.dtoClass = dto.getClass();
        return init(mysqlDtoInserter);
    }

    public static DtoInserter init(List<? extends DTO> dtos, Integer batchSize) {
        if (CollectionUtils.isEmpty(dtos)) {
            throw new DtoQueryException("插入的集合不能为空");
        }
        MysqlDtoInserter mysqlDtoInserter = new MysqlDtoInserter();
        mysqlDtoInserter.dtos = dtos;
        mysqlDtoInserter.batchSize = batchSize;
        mysqlDtoInserter.isBatchInsert = true;
        mysqlDtoInserter.dtoClass = dtos.get(0).getClass();
        return init(mysqlDtoInserter);
    }

    private static MysqlDtoInserter init(MysqlDtoInserter mysqlDtoInserter) {
        From from = mysqlDtoInserter.dtoClass.getDeclaredAnnotation(From.class);
        if (null == from) {
            throw new DtoQueryException("新增的实体类为空，请检查DTO:" + mysqlDtoInserter.dtoClass.getName() + "是否添加了from注解");
        }
        mysqlDtoInserter.insertSql = new StringBuilder();
        mysqlDtoInserter.valuesSql = new StringBuilder();
        mysqlDtoInserter.fromClass = from.value();
        mysqlDtoInserter.tableName = ClassUtils.getTableName(from.value());
        mysqlDtoInserter.declaredFields = mysqlDtoInserter.fromClass.getDeclaredFields();
        mysqlDtoInserter.params = new ArrayList<>();
        Class<?> superclass = mysqlDtoInserter.fromClass.getSuperclass();
        if (null != superclass) {
            mysqlDtoInserter.superFields = superclass.getDeclaredFields();
        }
        return mysqlDtoInserter;
    }

    @Override
    public void buildInsert() {
        this.insertSql.append("insert into ")
                .append(this.tableName).append("(");
        this.columns.forEach(it -> {
            this.insertSql.append(it.getField()).append(",");
        });
        this.insertSql.deleteCharAt(this.insertSql.lastIndexOf(","));
        this.insertSql.append(")");
    }

    @Override
    public void buildValues(DTO dto) {
        this.valuesSql.append("(");
        try {
            this.entity = this.fromClass.newInstance();
            BeanUtils.copyProperties(dto, this.entity);
        } catch (Exception e) {
            throw new DtoQueryException("初始化实例发生异常，请检查实例,entity:" + this.fromClass.getName());
        }
        this.columns.forEach(it -> {
            this.addParam(it.getFieldRef());
            this.valuesSql.append("?, ");
        });
        this.initializationInitState();
        this.valuesSql.deleteCharAt(this.valuesSql.lastIndexOf(","));
        this.valuesSql.append("),");
    }

    @Override
    public void addParam(Field field) {
        if (null == this.entity) {
            throw new DtoQueryException("将要插入的Dto为空，请检查数据");
        }

        if (initId(field)) {
            return;
        }

        if (initCreateBy(field)) {
            return;
        }
        if (initCreateTime(field)) {
            return;
        }

        if (initLastUpdateBy(field)) {
            return;
        }

        if (initLastUpdateTime(field)) {
            return;
        }

        if (initDeleted(field)) {
            return;
        }

        // 判断必填字段
        Column column = field.getDeclaredAnnotation(Column.class);
        try {
            field.setAccessible(true);
            Object value = field.get(this.entity);
            //如果不存在column 默认该字段可以为空，不需要判断该字段是否是空值
            if (null == column || column.nullable()) {
                this.params.add(value);
            } else {
                if (null == value) {
                    throw new DtoQueryException(field.getName() + "为必填字段，请检查数据，o:" + this.entity.toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private Boolean initDeleted(Field field) {
        if (this.ifDeletedInited) {
            return false;
        }
        Deleted deleted = field.getDeclaredAnnotation(Deleted.class);
        if (null == deleted) {
            return false;
        }
        this.params.add(deleted.value());
        this.ifDeletedInited = true;
        return true;
    }

    private Boolean initLastUpdateTime(Field field) {
        if (this.ifLastUpdateTimeInited) {
            return false;
        }
        LastUpdateTime lastUpdateTime = field.getDeclaredAnnotation(LastUpdateTime.class);
        if (null == lastUpdateTime) {
            return false;
        }
        Date date = new Date();
        if(null != InsertInit.lastUpdateByInit) {
            date = InsertInit.lastUpdateByInit.time();
        }
        this.params.add(date);
        return true;
    }

    private Boolean initLastUpdateBy(Field field) {
        if (this.ifLastUpdateByInited) {
            return false;
        }
        LastUpdateBy lastUpdateBy = field.getDeclaredAnnotation(LastUpdateBy.class);
        if (null == lastUpdateBy) {
            return false;
        }
        if (null == InsertInit.lastUpdateByInit) {
            try {
                field.setAccessible(true);
                this.params.add(field.get(this.entity));
            } catch (IllegalAccessException e) {
                throw new DtoQueryException(e);
            }
        } else {
            this.params.add(InsertInit.lastUpdateByInit.current());
        }
        this.ifLastUpdateByInited = true;
        return true;
    }


    private Boolean initCreateTime(Field field) {
        if (this.ifCreateTimeInited) {
            return false;
        }
        CreateTime createTime = field.getDeclaredAnnotation(CreateTime.class);
        if (null == createTime) {
            return false;
        }
        Date date = new Date();
        if(null != InsertInit.createByInit) {
            date = InsertInit.createByInit.time();
        }
        this.params.add(date);
        this.ifCreateTimeInited = true;
        return true;
    }

    private Boolean initCreateBy(Field field) {
        if (this.ifCreateByInited) {
            return false;
        }
        CreateBy createBy = field.getDeclaredAnnotation(CreateBy.class);
        if (null == createBy) {
            return false;
        }
        if (null == InsertInit.createByInit) {
            try {
                field.setAccessible(true);
                this.params.add(field.get(this.entity));
            } catch (IllegalAccessException e) {
                throw new DtoQueryException(e);
            }
        } else {
            this.params.add(InsertInit.createByInit.current());
        }
        this.ifCreateByInited = true;
        return true;
    }


    private Boolean initId(Field field) {
        if (this.ifIdInited) {
            return false;
        }
        Id id = field.getDeclaredAnnotation(Id.class);
        //如果是id 则直接将值给null 不取DTO 的值
        if (null == id) {
            return false;
        }
        Serializable generate = null;
        if (null == InsertInit.idInit) {
            generate = InsertInit.snowFlakeGenerator.nextId();
        } else {
            generate = InsertInit.idInit.generate();
        }
        this.params.add(generate);
        this.ifIdInited = true;
        return true;
    }
}
