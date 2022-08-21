package com.oohoo.spacestationspringbootstarter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/10
 */
@Configuration
@EnableConfigurationProperties(value = SpaceStationProperties.class)
public class SpaceStationAutoConfiguration {

    public static String TASK_EXECUTOR_NAME = null;
    private final ApplicationContext applicationContext;

    public SpaceStationAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringUtils getSpringUtils(SpaceStationProperties stationProperties) {
        TASK_EXECUTOR_NAME = stationProperties.getTaskExecutorName();
        SpringUtils springUtils = new SpringUtils();
        springUtils.setApplicationContext(applicationContext);
        return springUtils;
    }
}
