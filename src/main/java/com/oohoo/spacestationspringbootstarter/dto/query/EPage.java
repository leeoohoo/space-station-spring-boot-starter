package com.oohoo.spacestationspringbootstarter.dto.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/6
 */
@Data
public class EPage<T> implements Serializable {

    private Integer currentPage;

    private Integer pageSize;

    private Integer total;

    private List<T> list;




}
