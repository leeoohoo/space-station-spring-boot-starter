package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.manager.CdnManager;
import com.oohoo.spacestationspringbootstarter.dto.query.manager.SqlManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/16
 */
public class TestMian1 {

    public static void main(String[] args) {


        CdnManager ss = EQ.update(Test1.class)
                .set(Test1::getAge, 1)
                .set(TestWhat::getJob, "ss")
                .where()
                .eq(Test1::getAge, 1);
        SqlManager finish = ss.finish();
        System.out.println(finish.getUpdateSql());

        SqlManager finish1 = EQ.delete(Test1.class)
                .where().eq(Test1::getAge, 1).finish();
        System.out.println(finish1.getDeleteSql());


    }
}
