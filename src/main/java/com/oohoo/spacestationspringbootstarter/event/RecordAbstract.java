package com.oohoo.spacestationspringbootstarter.event;

import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/23
 */
@Component
public interface RecordAbstract {

    /**
     * 保存事件过程的返回值和
     * @param circulationRecord
     */
    void save(CirculationRecord circulationRecord);


}
