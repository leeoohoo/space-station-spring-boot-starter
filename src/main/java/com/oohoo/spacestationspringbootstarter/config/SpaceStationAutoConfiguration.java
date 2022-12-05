package com.oohoo.spacestationspringbootstarter.config;

import com.oohoo.spacestationspringbootstarter.dto.query.Butler;
import com.oohoo.spacestationspringbootstarter.dto.query.jpa.JpaSearch;
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
    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

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

    @Bean(name = "butler")
    @Scope(scopeName = "prototype")
    public Butler getButler() {
        return new JpaSearch(entityManager);
    }
}
