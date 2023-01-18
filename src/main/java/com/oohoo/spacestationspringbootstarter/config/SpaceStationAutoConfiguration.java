package com.oohoo.spacestationspringbootstarter.config;

import com.oohoo.spacestationspringbootstarter.dto.query.jpa.JpaButler;
import com.oohoo.spacestationspringbootstarter.dto.query.Butler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * @Description:
 * @Author: lei.d.li@capgemini.com
 * @CreateTime: 2022/8/10
 */
@Configuration
@EnableConfigurationProperties(value = SpaceStationProperties.class)
public class SpaceStationAutoConfiguration {

    public static String TASK_EXECUTOR_NAME = null;

    public static Integer DEFAULT_BATCH_SIZE = 50;
    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    EntityManagerFactory entityManagerFactory;

    public SpaceStationAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringUtils springUtils(SpaceStationProperties stationProperties) {
        TASK_EXECUTOR_NAME = stationProperties.getTaskExecutorName();
        Integer defaultBatchSize = Integer.valueOf(stationProperties.getDefaultBatchSize());

        DEFAULT_BATCH_SIZE =  defaultBatchSize <= 0  ? defaultBatchSize : DEFAULT_BATCH_SIZE;
        SpringUtils springUtils = new SpringUtils();
        springUtils.setApplicationContext(applicationContext);
        return springUtils;
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Butler butler() {
        return new JpaButler(entityManager);
    }
}
