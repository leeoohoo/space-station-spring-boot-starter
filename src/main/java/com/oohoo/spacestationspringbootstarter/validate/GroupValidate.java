package com.oohoo.spacestationspringbootstarter.validate;

import java.util.Map;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/3
 */
public interface GroupValidate extends Validate {


    /**
     * 根据分组进行数据校验
     * @param map 根据字段的名字作为map 的key,值作为value
     * @return 校验结果返回
     */
    boolean validate(Map<String, Object> map);
}
