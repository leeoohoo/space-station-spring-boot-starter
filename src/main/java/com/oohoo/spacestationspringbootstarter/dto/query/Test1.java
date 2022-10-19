package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Join;
import lombok.Data;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@Join(fromClazz = Test1.class, fromField = "name", joinClazz = Test1.class, joinField = "job")
@Join(fromClazz = Test1.class, fromField = "name", joinClazz = Test1.class, joinField = "age")
public class Test1  {

    private Integer id;

    private String name;

    private String job;

    private Integer age;
}
