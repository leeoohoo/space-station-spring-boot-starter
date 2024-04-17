package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LikeLocation;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.*;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.CdnScan;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.JoinScan;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.OrderByScan;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.SelectScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @since 11 November 2022
 */
public abstract class AbstractDtoQuery implements DtoQuery, SelectScan, JoinScan, CdnScan, OrderByScan {

    protected DTO dto;

    protected Class<? extends DTO> dtoClass;

    protected Class<?> fromClass;

    protected Field[] declaredFields;

    protected Field[] superFields;

    protected List<Column> columns;

    protected List<CdnContainer> cdnContainers;

    protected List<JoinContainer> joinContainers;

    protected List<OrderByContainer> orderByContainers;

    protected StringBuilder selectSql;

    protected StringBuilder cdnSql;

    protected StringBuilder joinSql;

    protected StringBuilder orderBySql;

    protected List<Object> params;

    protected boolean isBuild = false;

    @Override
    public void scan() {
        this.fromScan();
        this.selectScan();
        this.joinScan();
        this.cdnScan();
        this.orderByScan();
        this.build();

    }


    @Override
    public String getSql() {
        if (!isBuild) {
            StringBuilder order = new StringBuilder("");
            if (StringUtils.hasLength(this.orderBySql)) {
                order = this.orderBySql.deleteCharAt(this.orderBySql.lastIndexOf(","));

            }
            this.selectSql.append(this.joinSql).append(this.cdnSql).append(order);
            this.isBuild = true;
        }

        return this.selectSql.toString();
    }

    @Override
    public List<Object> getParams() {
        return this.params;
    }


    @Override
    public void orderByScan() {
//1. 获取所有Order
        Annotation[] declaredAnnotations = this.dtoClass.getDeclaredAnnotations();
        List<Annotation> annotations = Arrays.stream(declaredAnnotations).filter(it ->
                it.annotationType().equals(OrderBy.class) || it.annotationType().equals(OrderBy.List.class)
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(annotations)) {
            return;
        }
        Annotation annotation = annotations.get(0);

        this.orderByContainers = new ArrayList<>();
        if (annotation instanceof OrderBy.List) {
            OrderBy[] value = ((OrderBy.List) annotation).value();
            Arrays.stream(value).forEach(this::orderByContainer);
        }
        if (annotation instanceof OrderBy) {
            orderByContainer((OrderBy) annotation);
        }

    }

    @Override
    public void cdnScan() {
        this.cdnContainers = new ArrayList<>();
        Arrays.stream(this.declaredFields).forEach(it -> {
            AtomicReference<Condition> conditionAtomicReference = new AtomicReference<>(it.getDeclaredAnnotation(Condition.class));
            Integer order = 0;
            Boolean required = false;
            LogicEnum logic = null;
            LikeLocation likeLocation = null;
            String key = "";
            Class<?> table = null;
            for (Annotation annotation : it.getDeclaredAnnotations()) {
                Condition declaredAnnotation = annotation.annotationType().getDeclaredAnnotation(Condition.class);
                if (null != declaredAnnotation) {
                    conditionAtomicReference.set(annotation.annotationType().getDeclaredAnnotation(Condition.class));
                    order = (Integer) AnnotationUtils.getValue(annotation, "order");
                    required = (Boolean) AnnotationUtils.getValue(annotation, "required");
                    logic = (LogicEnum) AnnotationUtils.getValue(annotation, "logic");
                    likeLocation = (LikeLocation) AnnotationUtils.getValue(annotation, "likeLocation");
                    key = (String) AnnotationUtils.getValue(annotation, "key");
                    table = (Class<?>) AnnotationUtils.getValue(annotation, "table");
                }
            }
            Condition condition = conditionAtomicReference.get();
            if (null != condition) {
                //1. 存放cdn 容器
                CdnContainer cdnContainer =
                        CdnContainer.create(required, order, logic, likeLocation,
                                condition.op(), it, this.fromClass, this.dto, key, table);
                if (null != cdnContainer) {
                    this.cdnContainers.add(cdnContainer);
                }
            }
        });
    }

    @Override
    public void joinScan() {
        //1. 获取所有join
        Annotation[] declaredAnnotations = this.dtoClass.getDeclaredAnnotations();
        List<Annotation> annotations = Arrays.stream(declaredAnnotations).filter(it ->
                it.annotationType().equals(Join.class) || it.annotationType().equals(Join.List.class)
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(annotations)) {
            return;
        }
        // 将join放入容器中
        Annotation annotation = annotations.get(0);
        this.joinContainers = new ArrayList<>();
        if (annotation instanceof Join.List) {
            Join[] value = ((Join.List) annotation).value();
            Arrays.stream(value).forEach(this::joinContainer);
        }
        if (annotation instanceof Join) {
            joinContainer((Join) annotation);
        }
    }

    @Override
    public void selectScan() {
        //1. 排除所有不需要查询的字段
        List<Field> fields = Arrays.asList(this.declaredFields);
        if (null != this.superFields && this.superFields.length > 0) {
            fields.addAll(Arrays.asList(this.superFields));
        }

        fields = fields.stream()
                .filter(it -> null == it.getDeclaredAnnotation(Exclude.class))
                .collect(Collectors.toList());
        //2. 将所有查询的字段转换成column
        this.columns = ClassUtils.fieldsToColumns(this.dtoClass, fields);
    }

    @Override
    public void build() {
        this.selectSql = new StringBuilder();
        this.cdnSql = new StringBuilder();
        this.joinSql = new StringBuilder();
        this.orderBySql = new StringBuilder();
        this.params = new ArrayList<>();
        this.selectBuild();
        this.cdnBuild();
        this.joinBuild();
        this.orderBuild();
    }

    private void orderByContainer(OrderBy order) {
        this.orderByContainers.add(OrderByContainer.create(order));
    }

    private void joinContainer(Join join) {
        this.joinContainers.add(JoinContainer.create(join));
    }


    private void fromScan() {
        From declaredAnnotation = this.dtoClass.getDeclaredAnnotation(From.class);
        if (null == declaredAnnotation) {
            throw new DtoQueryException("缺少查询的主表，请尝试添加@From()注解");
        }
        this.fromClass = declaredAnnotation.value();
    }


}
