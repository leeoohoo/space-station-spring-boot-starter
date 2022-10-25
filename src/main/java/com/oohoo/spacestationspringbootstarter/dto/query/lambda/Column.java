package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.Test;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Entity;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;
import org.springframework.util.StringUtils;

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
        this.field = ClassUtils.getFiledName(fieldFunc);
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

    public static <T> Column create(SelectColumn<T, ?> selectColumn) {
        return create(selectColumn,"");
    }

    public String getSelectFieldSql() {
        return tableName + "." + field + (StringUtils.hasLength(alias) ? " as " + alias : "");
    }

    public String getCdnSql(OpEnum opEnum) {
        return tableName + "." + field  + opEnum.getOp() + " ? ";
    }



    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }


}
