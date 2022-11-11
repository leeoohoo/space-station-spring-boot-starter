package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import com.oohoo.spacestationspringbootstarter.dto.query.mysql.MysqlQuery;
import lombok.Data;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@Entity(name = "test")
@From(Test.class)
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "job")
@Join(fromClazz = Test.class, fromField = "name", joinClazz = Test.class, joinField = "age")
public class Test extends AbstractDtoQuery {

    private Integer id;

    @Eq
    private String name;

    @Like
    private String job;

    @Like
    private Integer age;

    private String userName;
}
