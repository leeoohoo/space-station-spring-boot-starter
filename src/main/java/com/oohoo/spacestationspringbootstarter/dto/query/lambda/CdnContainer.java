package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
@Data
public class CdnContainer {

    /**
     * 默认and
     */
    private LogicEnum logicSymbol;

    private Column column;

    private OpEnum opEnum;

    private Object value;

    /**
     * 如果为空则替换为 “？”
     */
    private Column column1;

    private Integer order;

    /**
     * 第一个字段
     */
    private Field field;

    /**
     * 第二个字段
     */
    private Field field1;

    private CdnContainer() {
    }

    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             OpEnum opEnum, SelectColumn<P, ?> column1,
                                             Object value, LogicEnum... logicSymbol) {
        CdnContainer tpCondition = new CdnContainer();
        if (null == logicSymbol || logicSymbol.length == 0) {
            tpCondition.logicSymbol = LogicEnum.AND;
        } else {
            tpCondition.logicSymbol = Arrays.stream(logicSymbol).findFirst().get();
        }
        tpCondition.column = Column.create(column);
        tpCondition.opEnum = opEnum;
        tpCondition.column1 = null == column1 ? null : Column.create(column1);
        tpCondition.value = value;
        tpCondition.order = 0;
        return tpCondition;
    }

    /**
     * 默认 Op 为 EQ
     *
     * @param column
     * @param column1
     * @param logicSymbol
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             SelectColumn<P, ?> column1, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, column1, logicSymbol);
    }

    /**
     * 默认等一某一个参数
     *
     * @param column
     * @param value
     * @param logicSymbol
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             Object value, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, null, value, logicSymbol);
    }

    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             OpEnum opEnum, Object value, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, null, value, logicSymbol);
    }

    /**
     * 默认逻辑判定符号后为“?”
     *
     * @param column
     * @param logicSymbol
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             OpEnum opEnum, LogicEnum... logicSymbol) {
        return create(column, opEnum, null, logicSymbol);
    }

    /**
     * 默认逻辑判定符号后为“?”
     * 默认 Op 为 EQ
     *
     * @param column
     * @param logicSymbol
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> CdnContainer create(SelectColumn<T, ?> column,
                                             LogicEnum... logicSymbol) {

        return create(column, OpEnum.EQ, null, logicSymbol);
    }

    public static CdnContainer create(Boolean required,
                                      Integer order,
                                      LogicEnum logicEnum,
                                      OpEnum opEnum,
                                      Field field,
                                      Class<?> fromClazz,
                                      Object dto) {
        field.setAccessible(true);
        CdnContainer cdnContainer = getValue(field, dto, required);
        if (null == cdnContainer) {
            return null;
        }
        cdnContainer.field = field;
        cdnContainer.column = Column.create(fromClazz, field);
        cdnContainer.logicSymbol = logicEnum;
        cdnContainer.opEnum = opEnum;
        cdnContainer.order = order;
        return cdnContainer;
    }

    private static CdnContainer getValue(Field field, Object dto, boolean required) {
        try {
            CdnContainer cdnContainer = new CdnContainer();
            field.setAccessible(true);
            Object value = field.get(dto);
            if (required && null == value) {
                throw new DtoQueryException("[fieldName:" + field.getName() + ",缺少参数，请传入必传的查询参数]");
            }
            if (null == value) {
                return null;
            }
            cdnContainer.value = value;
            return cdnContainer;
        } catch (IllegalAccessException e) {
            throw new DtoQueryException("获取参数发生异常");
        }
    }

}
