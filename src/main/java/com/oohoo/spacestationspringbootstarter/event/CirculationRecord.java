package com.oohoo.spacestationspringbootstarter.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CirculationRecord {

    /**
     * 事件源名称
     */
    private String happenName;

    /**
     * 事件源 & 事件接收器
     */
    private EventEnum type;

    /**
     * 事件接受器名称
     */
    private String triggerName;

    /**
     * 返回值的json
     */
    private String resultJson;

    /**
     * 入参的数组Json
     */
    private String paramsJsonArray;

    /**
     * 当前是第几步
     */
    private Integer step;

    /**
     * 是否异步
     */
    private EventEnum asyncOr;

    /**
     * 执行状态
     */
    private EventEnum status;

    /**
     * 异常信息
     */
    private String errorJson;

}
