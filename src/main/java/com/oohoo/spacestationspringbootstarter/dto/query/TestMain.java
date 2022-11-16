package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.lambda.CdnContainer;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
public class TestMain {

    public static void main(String[] args) {
        // 链式调用
        SqlManager fnish = EQ.find()
                .from(Test.class)
                .select(Test::getUserName, Test::getAge, Test::getUserName)
                .select(Test::getAge, "userAge")
                .left(Test1.class)
                .on(Test::getId, OpEnum.EQ, Test1::getAge)
                .left(Test1.class, "ceshi")
                .on(Test1::getJob, "ceshi", OpEnum.EQ, Test::getAge)
                .inner(Test.class)
                .on(Test::getAge, OpEnum.EQ, Test::getUserName,
                        CdnContainer.create(Test::getId, OpEnum.EQ, 1, LogicEnum.AND))
                .where().eq(Test::getAge, 1, true)
                .or()
                .likeLeft(Test::getName,"sss")
                .bracket()
                .eq(Test1::getJob, "ceshi")
                .eq(Test::getJob, "sss")
                .bracket()
                .finish();

        System.out.println(fnish.getSql());
        System.out.println(fnish.getParams());

        //-----通过dto 来生成sql与参数
        Test test = new Test();
        test.setAge(11);
        test.setName("ceshi");
        test.setJob("ss");

        DtoQuery sql = EQ.find(test);

        List<Object> params = sql.getParams();
        System.out.println(sql.getSql());
        System.out.println(params);
        

    }


}
