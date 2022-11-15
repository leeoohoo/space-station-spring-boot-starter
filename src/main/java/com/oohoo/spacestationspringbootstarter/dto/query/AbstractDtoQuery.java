package com.oohoo.spacestationspringbootstarter.dto.query;

import com.google.gson.*;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.ClassUtils;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.JoinContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.DtoSqlManager;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.CdnScan;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.JoinScan;
import com.oohoo.spacestationspringbootstarter.dto.query.scan.SelectScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 11 November 2022
 */
public abstract class AbstractDtoQuery implements DtoQuery, SelectScan, JoinScan, CdnScan, DtoSqlManager {

    protected DTO dto;

    protected Class<? extends DTO> dtoClass;

    protected Class<?> fromClass;

    protected Field[] declaredFields;

    protected List<Column> columns;

    protected List<CdnContainer> cdnContainers;

    protected List<JoinContainer> joinContainers;

    protected StringBuilder selectSql;

    protected StringBuilder cdnSql;

    protected StringBuilder joinSql;

    protected List<Object> params;

    @Override
    public void scan() {
        this.fromScan();
        this.selectScan();
        this.joinScan();
        this.cdnScan();
        this.build();
        System.out.println("");

    }



    @Override
    public String getSql() {
        return null;
    }

    @Override
    public List<Object> getParams() {
        return null;
    }

    @Override
    public void cdnScan() {
        this.cdnContainers = new ArrayList<>();
        Arrays.stream(this.declaredFields).forEach(it -> {
            AtomicReference<Condition> conditionAtomicReference = new AtomicReference<>(it.getDeclaredAnnotation(Condition.class));
            Integer order = 0;
            Boolean required = false;
            LogicEnum logic = LogicEnum.AND;
            for (Annotation annotation : it.getDeclaredAnnotations()) {
                conditionAtomicReference.set(annotation.annotationType().getDeclaredAnnotation(Condition.class));

                order = (Integer) AnnotationUtils.getValue(annotation, "order");
                required = (Boolean) AnnotationUtils.getValue(annotation, "required");
                logic = (LogicEnum) AnnotationUtils.getValue(annotation, "logic");
                System.out.println("");
            }
            Condition condition = conditionAtomicReference.get();
            if (null != condition) {
                //1. 存放cdn 容器
                CdnContainer cdnContainer =
                        CdnContainer.create(required, order, logic, condition.op(), it, this.fromClass, this.dto);
                this.cdnContainers.add(cdnContainer);
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
        List<Field> fields = Arrays.stream(this.declaredFields)
                .filter(it -> null == it.getDeclaredAnnotation(Exclude.class)).collect(Collectors.toList());
        //2. 将所有查询的字段转换成column
        this.columns = ClassUtils.fieldsToColumns(this.dtoClass, fields);
    }

    @Override
    public void build() {
        this.selectSql = new StringBuilder();
        this.cdnSql = new StringBuilder();
        this.joinSql = new StringBuilder();
        this.params = new ArrayList<>();
        this.selectBuild();
        this.cdnBuild();
        this.joinBuild();
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
