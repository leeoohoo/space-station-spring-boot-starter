package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.In;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.func.SelectColumn;

import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
public class TestMain {

    public static void main(String[] args) {

        Test test = new Test();
        test.creat()
                .select(Test::getAge)
                .left(Test1.class)
                .on(Test::getId, OpEnum.EQ, Test1::getAge)
                .where().eq(Test::getAge,true)
                .or()
                .eq(Test1::getJob,"ceshi");


        test.select().where().eq(Test::getAge).in(Test::getAge, new ArrayList<Integer>()).bracket().or();
        test.creat();
        test.select();
        test.join();
        test.test(Test::getAge);
        test.test(Test::getId);
        test.test(Test1::getAge);
    }


}
