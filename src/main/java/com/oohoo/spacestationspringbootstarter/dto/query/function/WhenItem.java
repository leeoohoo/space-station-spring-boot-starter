package com.oohoo.spacestationspringbootstarter.dto.query.function;

import com.oohoo.spacestationspringbootstarter.dto.query.TestWhat;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import lombok.Data;

/**
 * @author Lei Li. leeoohoo@gmail.com
 * @Description
 * @since 23 November 2022
 */
@Data
public class WhenItem {

    private CdnContainer cdnContainer;

    private SelectColumn<?, ?> selectColumn;

    private WhenItem() {
    }

    public static <T> WhenItem when(CdnContainer cdnContainer, SelectColumn<T, ?> selectColumn) {
        WhenItem whenItem = new WhenItem();
        whenItem.setCdnContainer(cdnContainer);
        whenItem.setSelectColumn(selectColumn);
        return whenItem;
    }
}
