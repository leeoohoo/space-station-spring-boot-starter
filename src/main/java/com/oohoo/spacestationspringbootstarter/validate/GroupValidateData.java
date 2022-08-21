/*
 * @Description:
 * @version:
 * @Author: leeoohoo
 * @Date: 2020-03-11 10:50:20
 * @LastEditors: leeoohoo
 * @LastEditTime: 2020-03-11 10:50:30
 */
package com.oohoo.spacestationspringbootstarter.validate;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 当前线程的变量<integer></>
 * email leeoohoo@gmail.com
 * system
 *
 * @author lee
 * @date 2019/10/11
 */
@Component
public class GroupValidateData {

    /**
     * 合同字段列表
     */
    private static final ThreadLocal<Map<String,Map<String,Object>>> CONTRACT_FIELD_THREAD_LOCAL =  new ThreadLocal<>();

    /**
     * 设置当前线程的变量
     * @param contractFieldList
     */
    public static void setContractFieldThreadLocal(Map<String,Map<String,Object>> contractFieldList) {
        CONTRACT_FIELD_THREAD_LOCAL.set(contractFieldList);
    }

    /**
     * 取得当前线程中的变量
     */
    public static Map<String,Map<String,Object>> getContractFieldThreadLocal() {
        return CONTRACT_FIELD_THREAD_LOCAL.get();
    }

    /**
     * 清除当前线程用户
     */
    public static void clearContractFieldThreadLocal() {
        CONTRACT_FIELD_THREAD_LOCAL.remove();
    }

}
