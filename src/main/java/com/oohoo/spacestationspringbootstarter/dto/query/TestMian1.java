package com.oohoo.spacestationspringbootstarter.dto.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/16
 */
public class TestMian1 {

    public static void main(String[] args) {


        List<TestDto> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestDto testDto = new TestDto();
            testDto.setTestName("ceshi");
            testDto.setId(1L);
            testDto.setName("ss");
            testDto.setAge(1);
            list.add(testDto);
        }
        DtoInserter insert = EQ.insert(list,43);
        System.out.println(insert.getBatchInsertContainers());
        System.out.println(insert.getBatchInsertContainers());
        System.out.println(insert.getParams());


    }
}
