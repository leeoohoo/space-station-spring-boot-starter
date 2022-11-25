package com.oohoo.spacestationspringbootstarter.dto.query;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 23 November 2022
 */
@Data
@Entity
public class TestDto implements DTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long dtoId;

    private String dtoName;


}
