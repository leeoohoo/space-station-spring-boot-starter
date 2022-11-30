package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.LogicEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.enums.OrderByEnum;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GeneralFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.GroupByFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.SqlFunction;
import com.oohoo.spacestationspringbootstarter.dto.query.function.WhenItem;
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
                .from(TestWhat.class)
                .select(TestWhat::getUserName, TestWhat::getAge, TestWhat::getUserName)
                .select(TestWhat::getUserName,"userName")
                .select(TestWhat::getAge, "userAge")
                .select(TestDto.class)
                .select(EF.abs(TestDto::getDtoId))
                .select(EF.sum(Test1::getJob))
                .left(Test1.class)
                .on(TestWhat::getId, OpEnum.EQ, Test1::getAge)
                .left(Test1.class, "ceshion1=1leftjiontest1asceshi")
                .on(Test1::getJob, "ceshi1", OpEnum.EQ, TestWhat::getAge)
                .inner(TestWhat.class)
                .on(TestWhat::getAge, OpEnum.EQ, TestWhat::getUserName,
                        CdnContainer.create(TestWhat::getId, OpEnum.EQ, 1, LogicEnum.AND))
                .where().eq(TestWhat::getAge, 1, true)
                .or()
                .likeLeft(TestWhat::getName, "sss")
                .bracket()
                .eq(TestWhat::getName, "ceshi")
                .eq(TestWhat::getJob, "sss")
                .bracket()
                .having()
                .havBracket()
                .addCdn(EF.sum(Test1::getJob),OpEnum.EQ,1)
                .havOr()
                .addCdn(EF.avg(Test1::getAge),OpEnum.EQ,Test1::getAge)
                .havBracket()
                .order()
                .by(Test1::getJob, OrderByEnum.DESC)
                .by(Test1::getAge)
                .finish();

        System.out.println(fnish.getSql());
        System.out.println(fnish.getParams());

        //-----通过dto 来生成sql与参数
        TestWhat test = new TestWhat();
        test.setAge(11);
        test.setName("ceshi");
        test.setJob("ss");

        DtoQuery sql = EQ.find(test);

        List<Object> params = sql.getParams();
        SqlFunction abs = EF.addDate(TestWhat::getAge, 1, TestDto::getDtoId);
        SqlFunction generalFunction = EF.charLength(TestWhat::getAge);
        SqlFunction concat = EF.concat(TestWhat::getName,
                "null",
                TestWhat::getAge,
                TestWhat::getAge,
                TestWhat::getAge);
        SqlFunction generalFunction1 = EF.caseWhen(TestDto::getDtoName,
                WhenItem.when(CdnContainer.create(TestWhat::getAge, OpEnum.EQ, Test1::getAge), Test1::getAge),
                WhenItem.when(CdnContainer.create(TestWhat::getAge, OpEnum.EQ, 1), Test1::getAge)

        );
        GroupByFunction sum = EF.sum(Test1::getId,Test1::getAge);
        GroupByFunction sum1 = EF.sum(EF.abs(Test1::getJob), Test1::getAge).distinct();
        String funcSql = sum1.getFuncSql();
        sum.getFuncSql();
        System.out.println(sql.getSql());

        System.out.println(params);


    }




}
