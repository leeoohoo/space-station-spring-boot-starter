package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;
import lombok.Data;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "job")
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "age")
public class Test implements DtoQuery<Test, SelectColumn<Test,?>> {

    private Integer id;

    private String name;

    private String job;

    private Integer age;
}
