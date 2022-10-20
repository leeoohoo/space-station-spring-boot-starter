package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Eq;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Like;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "job")
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "age")
public class Test implements DtoQuery<Test> {

    private Integer id;

    @Eq
    private String name;

    @Like
    private String job;

    private Integer age;
}
