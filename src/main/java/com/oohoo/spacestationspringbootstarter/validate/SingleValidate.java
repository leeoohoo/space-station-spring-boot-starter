package com.oohoo.spacestationspringbootstarter.validate;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/3
 */
public interface SingleValidate<T> extends Validate {
    /**
     * 校验
     * @param obj 前端传的值
     * @return 返回校验结果
     */
    boolean validate(T obj);
}
