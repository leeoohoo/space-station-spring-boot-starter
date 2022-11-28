package com.oohoo.spacestationspringbootstarter.dto.query;

import com.oohoo.spacestationspringbootstarter.dto.query.annotation.Exclude;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.From;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.JoinColumn;
import lombok.Data;


/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 23 November 2022
 */
@Data
@From(Test1.class)
public class TestDto implements DTO {
    private Long id;

    private Long dtoId;

    private String dtoName;

    @Exclude
    private String home;


    @JoinColumn(joinClass = TestWhat.class,columnName = "name")
    private String testName;


}
