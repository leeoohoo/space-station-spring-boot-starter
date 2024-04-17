package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.ClazzIsNull;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LikeLocation;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;


/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 21 October 2022
 */
@Data
public class Column {

    private Class<?> clazz;
    private String tableName;

    private String field;

    private String alias;

    private Field fieldRef;

    private Object value;


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
        this.fieldRef = field;
    }

    public static Column create(Class<?> clazz, String fieldFunc, String alias) {
        if(alias.trim().contains(" ")) {
            throw new DtoQueryException("别名不允许有空格");
        }
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
        return tableName + "." + this.field + opEnum.getOp() + " ? ";
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
    public String getCdnSql(OpEnum opEnum,Class<?> clazzIsNull,String key) {
        String table = "";
        if(null == clazzIsNull || clazzIsNull.isAssignableFrom(ClazzIsNull.class))  {
            table = this.tableName;
        }else {
            table = ClassUtils.getTableName(clazzIsNull);
        }
        String resultField = StringUtils.hasLength(key) ? ClassUtils.camelToUnderline(key) : field;
        return table + "." + resultField + opEnum.getOp() + " ? ";
    }

    public String getCdnSql(OpEnum opEnum, LikeLocation leftOrRight,Class<?> clazzIsNull,String key) {
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

        String table = "";
        if(null == clazzIsNull || clazzIsNull.isAssignableFrom(ClazzIsNull.class))  {
            table = this.tableName;
        }else {
            table = ClassUtils.getTableName(clazzIsNull);
        }
        String resultField = StringUtils.hasLength(key) ? ClassUtils.camelToUnderline(key) : field;
        return table + "." + resultField + opEnum.getOp() + placeholder;
    }

    public String getOnSql() {
        return " " + tableName + "." + field + " ";
    }


}
