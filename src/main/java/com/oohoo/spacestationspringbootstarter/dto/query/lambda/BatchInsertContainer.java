package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/15
 */
@Data
public class BatchInsertContainer {

    private String sql;

    private List<Object> params;
}
