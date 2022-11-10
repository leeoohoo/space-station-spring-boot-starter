package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
public class TestMain {

    public static void main(String[] args) {

        Test test = new Test();
        test.setAge(11);

        SqlManager fnish = test.create()
                .from(Test.class)
                .select(Test::getUserName, Test::getAge, Test::getUserName)
                .select(Test::getAge, "userAge")
                .left(Test1.class)
                .on(Test::getId, OpEnum.EQ, Test1::getAge)
                .left(Test1.class, "ceshi")
                .on(Test1::getJob, "ceshi", OpEnum.EQ, Test::getAge)
                .inner(Test.class)
                .on(Test::getAge, OpEnum.EQ, Test::getUserName, Condition.create(Test::getId, OpEnum.EQ, 1, LogicEnum.AND))
                .where().eq(Test::getAge, 1, true)
                .eq(Test::getAge)
                .or()
                .bracket()
                .eq(Test1::getJob, "ceshi")
                .eq(Test::getJob, "sss")
                .bracket()
                .fnish();

        System.out.println(fnish.getSql());
        System.out.println(fnish.getParams());

    }


}
