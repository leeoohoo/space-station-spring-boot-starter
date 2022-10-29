package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.Column;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 October 2022
 */
@Data
public class Condition {

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

    private Condition() {
    }

    public static <T, P> Condition create(SelectColumn<T, ?> column,
                                          OpEnum opEnum, SelectColumn<P, ?> column1,
                                          Object value, LogicEnum... logicSymbol) {
        Condition tpCondition = new Condition();
        if (null == logicSymbol || logicSymbol.length == 0) {
            tpCondition.logicSymbol = LogicEnum.AND;
        } else {
            tpCondition.logicSymbol = Arrays.stream(logicSymbol).findFirst().get();
        }
        tpCondition.column = Column.create(column);
        tpCondition.opEnum = opEnum;
        tpCondition.column1 = null == column1 ? null : Column.create(column1);
        tpCondition.value = value;
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
    public static <T, P> Condition create(SelectColumn<T, ?> column,
                                          SelectColumn<P, ?> column1, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, column1, logicSymbol);
    }

    /**
     * 默认等一某一个参数
     * @param column
     * @param value
     * @param logicSymbol
     * @return
     * @param <T>
     * @param <P>
     */
    public static <T, P> Condition create(SelectColumn<T, ?> column,
                                          Object value, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, null,value, logicSymbol);
    }

    public static <T, P> Condition create(SelectColumn<T, ?> column,
                                          OpEnum opEnum, Object value, LogicEnum... logicSymbol) {
        return create(column, OpEnum.EQ, null,value, logicSymbol);
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
    public static <T, P> Condition create(SelectColumn<T, ?> column,
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
    public static <T, P> Condition create(SelectColumn<T, ?> column,
                                          LogicEnum... logicSymbol) {

        return create(column, OpEnum.EQ, null, logicSymbol);
    }
}
