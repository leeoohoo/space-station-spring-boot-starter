package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.EntityName;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.OrderBy;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OrderByEnum;
import lombok.Data;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@EntityName(name = "test1")
@Join(fromClazz = Test1.class, fromField = "name", joinClazz = Test1.class, joinField = "job")
@Join(fromClazz = Test1.class, fromField = "name", joinClazz = Test1.class, joinField = "age")
@OrderBy(table = Test1.class,field = "name",orderType = OrderByEnum.DESC)
public class Test1 {

    private Integer id;

    private String name;

    private String job;

    private Integer age;
}
