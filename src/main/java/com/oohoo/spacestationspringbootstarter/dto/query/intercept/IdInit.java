package com.oohoo.spacestationspringbootstarter.dto.query.intercept;

import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/12/16
 */
public interface IdInit  {

    Serializable generate();
}
