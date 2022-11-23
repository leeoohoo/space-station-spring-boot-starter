package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.*;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
@Data
@Entity()
@From(TestWhat.class)
@Join(fromClazz = TestWhat.class, fromField = "name", joinClazz = TestWhat.class, joinField = "job")
@Join(fromClazz = TestWhat.class,fromField = "id",joinClazz = Test1.class,joinField = "id")
public class TestWhat implements DTO {

    @Id
    private Integer id;

    @Eq(order = 1,required = true)

    private String name;

    @LikeLeft
    private String job;

    @Like(required = true)
    private Integer age;


    @JoinColumn(joinClass = Test1.class,columnName = "userName")
    private String userName;
}
