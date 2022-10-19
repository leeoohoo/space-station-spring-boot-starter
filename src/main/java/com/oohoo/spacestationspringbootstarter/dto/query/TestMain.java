package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.enums.OpEnum;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/9/1
 */
public class TestMain {

    public static void main(String[] args) {

        Test test = new Test();

        test.select();
        test.test(Test::getAge);
        test.test(Test::getId);
    }




}
