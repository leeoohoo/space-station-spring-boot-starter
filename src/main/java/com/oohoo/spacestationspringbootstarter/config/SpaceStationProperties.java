package com.oohoo.spacestationspringbootstarter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/10
 */

@ConfigurationProperties(prefix = "station")
@Data
public class SpaceStationProperties {


    @Value("taskExecutorName")
    private String taskExecutorName;

    @Value("defaultBatchSize")
    private String defaultBatchSize;


}
