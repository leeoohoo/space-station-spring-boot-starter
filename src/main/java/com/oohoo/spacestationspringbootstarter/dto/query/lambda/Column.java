package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.Test;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Entity;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LikeLocation;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
@Data
public class Column {

    private Class<?> clazz;
    private String tableName;

    private String field;

    private String alias;


    private Column(Class<?> clazz, String fieldFunc, String alias) {
        this.tableName = ClassUtils.getTableName(clazz);
        String filedName = ClassUtils.getFiledName(fieldFunc);
        this.field = ClassUtils.camelToUnderline(filedName);
        this.alias = StringUtils.hasLength(alias) ? alias : filedName;
        this.clazz = clazz;
    }

    private Column(Class<?> clazz, Field field, String alias) {
        this.tableName = ClassUtils.getTableName(clazz);
        this.field = ClassUtils.camelToUnderline(field.getName());
        this.alias = alias;
        this.clazz = clazz;
    }

    public static Column create(Class<?> clazz, String fieldFunc, String alias) {
        return new Column(clazz, fieldFunc, alias);
    }

    public static <T> Column create(SelectColumn<T, ?> selectColumn, String alias) {
        SerializedLambda resolve = SerializedLambda.resolve(selectColumn);
        Class<?> clazz = resolve.getImplClass();
        String implMethodName = resolve.getImplMethodName();
        return create(clazz, implMethodName, alias);
    }

    public static <T> Column create(Class<?> clazz, Field field, String alias) {
        return new Column(clazz, field, StringUtils.hasLength(alias) ? alias : field.getName());
    }

    public static <T> Column create(Class<?> clazz, Field field) {
        return create(clazz, field, null);
    }

    public static <T> Column create(SelectColumn<T, ?> selectColumn) {
        return create(selectColumn, "");
    }

    public String getSelectFieldSql() {
        return tableName + "." + field + (StringUtils.hasLength(alias) ? " as " + alias : "");
    }

    public String getCdnSql(OpEnum opEnum) {
        return tableName + "." + field + opEnum.getOp() + " ? ";
    }

    public String getCdnSql(OpEnum opEnum, LikeLocation leftOrRight) {
        String placeholder = " ? ";
        if(OpEnum.LIKE.equals(opEnum)) {
            switch (leftOrRight){
                case ALL:
                    placeholder = " concat('%',? ,'%') ";
                    break;
                case LEFT:
                    placeholder = " concat('%',? ) ";
                    break;
                case RIGHT:
                    placeholder = " concat(? ,'%') ";
                    break;
            }
        }
        return tableName + "." + field + opEnum.getOp() + placeholder;
    }

    public String getOnSql() {
        return " " + (StringUtils.hasLength(alias) ? alias : tableName) + "." + field + " ";
    }





}
