package com.xinaml.frame.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.engine.*;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Component
public class ActivitiConf {
    @Autowired
    private DruidDataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;


    @Bean
    public ProcessEngine processEngine() throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        //自动部署已有的流程文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "/templates/processes/*.bpmn");
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setDeploymentResources(resources);
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDbIdentityUsed(true);
        return configuration.buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

}
