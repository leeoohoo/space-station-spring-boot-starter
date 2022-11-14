package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Condition;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.JoinEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
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
public class Test implements DTO {

    private Integer id;

    @Eq(order = 1,required = true)
    private String name;

    @Like(required = true)
    private String job;

    @Like(required = true)
    private Integer age;


    private String userName;
}
