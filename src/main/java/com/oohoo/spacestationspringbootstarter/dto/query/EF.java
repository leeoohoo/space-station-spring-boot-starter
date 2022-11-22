package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.SqlFunctionEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import kotlin.Function;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 22 November 2022
 */
public class EF {

    /**
     * 取绝对值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Abs'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction abs(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.ABS, "Abs", selectColumn, alias);
    }


    /**
     * 返回大于或等于X 的最小整数（向上取整）
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Ceil'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction ceil(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.CEIL, "Ceil", selectColumn, alias);
    }

    /**
     * 返回小于或等于X 的最小整数（向下取整）
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Floor'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction floor(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.FLOOR, "Floor", selectColumn, alias);
    }

    /**
     * 返回根据字段值生成的0->1 的随机小数
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Rand'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction rand(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.RAND, "Rand", selectColumn, alias);
    }

    /**
     * 返回X 的符号, 若X是负数,0,正数 分别返回 -1,0,1
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Sign'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction sign(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.SIGN, "Sign", selectColumn, alias);
    }

    /**
     * 返回离x 最近的整数
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Round'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction round(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.ROUND, "Round", selectColumn, alias);
    }

    /**
     * 保留 digits 位数, 会四舍五入
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'RoundDigits'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R, J> GeneralFunction round(SelectColumn<T, ?> selectColumn, SelectColumn<J, ?> selectColumn1, SelectColumn<R, ?>... alias) {
        return doubleParameter(SqlFunctionEnum.ROUND_DIGITS, "RoundDigits", selectColumn1, selectColumn, alias);
    }

    /**
     * 返回X 的Y次方
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Pow'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R, J> GeneralFunction pow(SelectColumn<T, ?> selectColumn, SelectColumn<J, ?> selectColumn1, SelectColumn<R, ?>... alias) {
        return doubleParameter(SqlFunctionEnum.POW, "Pow", selectColumn1, selectColumn, alias);
    }

    /**
     * 保留 digits 位数, 不会四舍五入
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Truncate'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R, J> GeneralFunction truncate(SelectColumn<T, ?> selectColumn, SelectColumn<J, ?> selectColumn1, SelectColumn<R, ?>... alias) {
        return doubleParameter(SqlFunctionEnum.TRUNCATE, "Truncate", selectColumn1, selectColumn, alias);
    }

    /**
     * 保留 digits 位数, 不会四舍五入
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Truncate'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R, J> GeneralFunction mod(SelectColumn<T, ?> selectColumn, SelectColumn<J, ?> selectColumn1, SelectColumn<R, ?>... alias) {
        return doubleParameter(SqlFunctionEnum.MOD, "Mod", selectColumn1, selectColumn, alias);
    }

    /**
     * 返回 X 的平方根
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Sqrt'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction sqrt(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.SQRT, "Sqrt", selectColumn, alias);
    }


    /**
     * 返回e（是一个常数,2.71828）的 x次方
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Exp'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction exp(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.EXP, "Exp", selectColumn, alias);
    }


    /**
     * 返回自然数对数（以e 为底的对数）
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Log'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction log(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.LOG, "Log", selectColumn, alias);
    }

    /**
     * 返回自然数对数（以10为底的对数）
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Log10'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction log10(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.LOG10, "Log10", selectColumn, alias);
    }


    /**
     * 将角度转换为度数
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Radians'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction radians(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.RADIANS, "Radians", selectColumn, alias);
    }

    /**
     * 将度数转换为角度
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Degrees'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction degrees(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.DEGREES, "Degrees", selectColumn, alias);
    }

    /**
     * 求正弦值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Sin'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction sin(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.SIN, "Sin", selectColumn, alias);
    }

    /**
     * 求反正弦值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Asin'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction asin(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.ASIN, "Asin", selectColumn, alias);
    }

    /**
     * 求余弦值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cos'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction cos(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.COS, "Cos", selectColumn, alias);
    }

    /**
     * 求反余弦值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Acos'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction acos(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.ACOS, "Acos", selectColumn, alias);
    }


    /**
     * 求正切值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Tan'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction tan(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.TAN, "Tan", selectColumn, alias);
    }

    /**
     * 求反正切值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Atan'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction atan(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.ATAN, "Atan", selectColumn, alias);
    }

    /**
     * 求余切值
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cot'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction cot(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.COT, "Cot", selectColumn, alias);
    }


    /**
     * 返回字符串s 的字符长度
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cot'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction charLength(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.CHAR_LENGTH, "CharLength", selectColumn, alias);
    }

    /**
     * 返回字符串s 的字符长度
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cot'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction length(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.LENGTH, "Length", selectColumn, alias);
    }

    /**
     * 返回字符串s 并大写
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cot'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction upper(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.UPPER, "Upper", selectColumn, alias);
    }

    /**
     * 返回字符串s 并小写
     *
     * @param selectColumn 字段
     * @param alias        别名，不给就会在字段后面加 'Cot'作为默认别名
     * @param <T>          实体的类型
     * @return sql 函数
     */
    @SafeVarargs
    public static <T, R> GeneralFunction lower(SelectColumn<T, ?> selectColumn, SelectColumn<R, ?>... alias) {
        return singleParameter(SqlFunctionEnum.LOWER, "Lower", selectColumn, alias);
    }


    /**
     * 合并字段
     * @param alias 别名 接受结果DTO 中的某字段
     * @param separator 字段合并的分割符 如果不传默认没有
     * @param selectColumn 要合并的字段 只能合并同一个表的字段
     * @return sql 函数
     * @param <T> 别名的类型
     * @param <R> 实体的类型
     */
    @SafeVarargs
    public static <T,R> GeneralFunction concat(SelectColumn<T, ?> alias,
                                                                String separator,
                                                                SelectColumn<R,?>... selectColumn) {
        return moreParameter(SqlFunctionEnum.CONCAT, alias, separator, selectColumn);
    }

    @SafeVarargs
    private static <T, R> GeneralFunction singleParameter(
            SqlFunctionEnum sqlFunctionEnum,
            String aliasSuffix,
            SelectColumn<T, ?> selectColumn,
            SelectColumn<R, ?>... alias) {
        Column column = Column.create(selectColumn);
        String defaultAlias = column.getAlias() + aliasSuffix;
        if (null != alias && alias.length > 0) {
            defaultAlias = Column.create(alias[0]).getAlias();
        }
        String value = sqlFunctionEnum.getValue();
        String replace = value.replace("?", column.getTableName() + "." + column.getField());
        return GeneralFunction.create(sqlFunctionEnum, replace, defaultAlias);
    }

    @SafeVarargs
    private static <T, R, J> GeneralFunction doubleParameter(
            SqlFunctionEnum sqlFunctionEnum,
            String aliasSuffix,
            SelectColumn<J, ?> secondParam,
            SelectColumn<T, ?> selectColumn,
            SelectColumn<R, ?>... alias) {
        Column column = Column.create(selectColumn);
        Column column1 = Column.create(secondParam);
        String defaultAlias = column.getAlias() + aliasSuffix;
        if (null != alias && alias.length > 0) {
            defaultAlias = Column.create(alias[0]).getAlias();
        }
        String value = replacePlaceholder(sqlFunctionEnum.getValue(),
                column.getTableName() + "." + column.getField(),
                column1.getTableName() + "." + column1.getField());
        return GeneralFunction.create(sqlFunctionEnum, value, defaultAlias);
    }

    private static <T,R> GeneralFunction moreParameter(SqlFunctionEnum sqlFunctionEnum,
                                                     SelectColumn<T, ?> alias,
                                                     String separator,
                                                     SelectColumn<R, ?>... selectColumns) {
        Column aliasColumn = Column.create(alias);
        String defaultAlias = aliasColumn.getAlias();
        List<Object> list = new ArrayList<>();
        Arrays.stream(selectColumns).forEach(it -> {
            Column column = Column.create(it);
            list.add(column.getTableName() + "." + column.getField());
            if(StringUtils.hasLength(separator)) {
                list.add("'"+separator+"'");
            }
        });
        String funcSql = replacePlaceholder(sqlFunctionEnum.getValue(), list);
        return GeneralFunction.create(sqlFunctionEnum, funcSql, defaultAlias);
    }


    private static String replacePlaceholder(String str, Object... objects) {
        StringBuilder result = new StringBuilder("");
        char[] chars = str.toCharArray();
        int index = 0;
        for (char aChar : chars) {
            if ('?' == aChar) {
                result.append(objects[index]);
                index++;
            } else {
                result.append(aChar);
            }
        }
        return result.toString();
    }

}
