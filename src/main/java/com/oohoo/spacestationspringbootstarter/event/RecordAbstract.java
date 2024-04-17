package com.oohoo.spacestationspringbootstarter.event;

import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/23
 */
@Component
public interface RecordAbstract {

    /**
     * 保存事件接收器的执行记录
     *
     * @param circulationRecord
     */
    void saveTrigger(CirculationRecord circulationRecord);


    /**
     * 保存事件发生器的执行记录
     * @param circulationRecord
     */
    void saveHappen(CirculationRecord circulationRecord);


}
