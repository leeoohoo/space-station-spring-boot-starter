package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Exclude;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.JoinColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.BatchInsertContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/14
 */
public abstract class AbstractDtoInserter implements DtoInserter {

    protected DTO dto;

    protected List<? extends DTO> dtos;

    protected Class<? extends DTO> dtoClass;

    protected Class<?> fromClass;

    protected Field[] declaredFields;

    protected Field[] superFields;

    protected List<Column> columns;

    protected String tableName;

    protected Boolean isBatchInsert = false;

    protected StringBuilder resultSql;

    protected StringBuilder insertSql;

    protected StringBuilder valuesSql;

    protected List<Object> params;

    protected Integer batchSize;

    protected Object entity;

    protected Field idField;

    protected Boolean isBuild = false;

    protected List<BatchInsertContainer> batchInsertContainers;

    protected boolean ifIdInited = false;

    protected boolean ifCreateByInited = false;
    protected boolean ifLastUpdateByInited = false;
    protected boolean ifCreateTimeInited = false;
    protected boolean ifLastUpdateTimeInited = false;

    protected boolean ifDeletedInited = false;

    @Override
    public void scan() {
        this.insertScan();
        if (isBatchInsert) {
            this.buildBatch();
        } else {
            this.build();
        }
    }


    @Override
    public String getInsertOneSql() {
        if (this.isBatchInsert) {
            throw new DtoQueryException("批量插入请通过“getBatchInsertContainers”方法获取执行sql与参数");
        }
        return this.getSql();
    }

    @Override
    public Object getEntity() {
        return this.entity;
    }

    @Override
    public List<Object> getParams() {
        if (this.isBatchInsert) {
            throw new DtoQueryException("批量插入请通过“getBatchInsertContainers”方法获取执行sql与参数");
        }

        return this.params;
    }

    private void insertScan() {
        List<Field> fields = new ArrayList<>(Arrays.asList(this.declaredFields));
        fields.addAll(Arrays.asList(this.superFields));
        //将需要排除的列及外连接的列都去除
        fields = fields.stream().filter(field -> {
            Exclude exclude = field.getDeclaredAnnotation(Exclude.class);
            JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
            return null == exclude && null == joinColumn;
        }).collect(Collectors.toList());
        this.columns = ClassUtils.fieldsToColumns(this.fromClass, fields);
    }

    @Override
    public void build() {
        this.buildInsert();
        this.buildValues(this.dto);
    }


    @Override
    public void buildBatch() {
        batchInsertContainers = new ArrayList<>();
        this.buildInsert();
        AtomicReference<Integer> count = new AtomicReference<>(1);
        for (DTO dto : this.dtos) {
            this.buildValues(dto);
            if (count.get() % batchSize == 0) {
                BatchInsertContainer batchInsertContainer = new BatchInsertContainer();
                batchInsertContainer.setSql(this.getSql());
                batchInsertContainer.setParams(this.params);
                batchInsertContainers.add(batchInsertContainer);
                this.resultSql = new StringBuilder();
                this.valuesSql = new StringBuilder();
                this.isBuild = false;
                this.params = new ArrayList<>();

            }

            count.getAndSet(count.get() + 1);
        }
        if(StringUtils.hasLength(this.valuesSql)) {
            BatchInsertContainer batchInsertContainer = new BatchInsertContainer();
            batchInsertContainer.setSql(this.getSql());
            batchInsertContainer.setParams(this.params);
            batchInsertContainers.add(batchInsertContainer);
            this.resultSql = new StringBuilder();
            this.valuesSql = new StringBuilder();
            this.params = new ArrayList<>();
            this.isBuild = true;
        }

    }

    @Override
    public List<BatchInsertContainer> getBatchInsertContainers() {
        if (!isBatchInsert) {
            throw new DtoQueryException("单个插入无法获取批次插入的容器");
        }


        return this.batchInsertContainers;
    }

    private String getSql() {
        if (isBuild) {
            return this.resultSql.toString();
        }
        resultSql = new StringBuilder();
        this.resultSql.append(insertSql).append(" values ").append(this.valuesSql.deleteCharAt(this.valuesSql.lastIndexOf(",")));
        this.isBuild = true;
        return resultSql.toString();
    }


    protected void initializationInitState() {
        this.ifCreateByInited = false;
        this.ifCreateTimeInited = false;
        this.ifLastUpdateByInited = false;
        this.ifLastUpdateTimeInited = false;
        this.ifIdInited = false;
        this.ifDeletedInited = false;
    }
}
