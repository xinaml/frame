package com.xinaml.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableAutoConfiguration // 自动装备
@ComponentScan(basePackages = {"com.xinaml.frame"}) //spring 扫描
@EnableJpaRepositories(basePackages = {"com.xinaml.frame.rep"}) //持久化接口
@EntityScan(basePackages = {"com.xinaml.frame.entity"}, basePackageClasses = Jsr310JpaConverters.class)
//扫描实体映射类，Jsr310JpaConverters：对日期的转换处理
@PropertySource({"classpath:config.properties"}) //加载配置文件
@EnableTransactionManagement //开启事务
@EnableSwagger2 //Swagger api文档开启
@ServletComponentScan //druid 监控中心开启
@EnableCaching// 开启缓存
public class AppRoot {

    public static void main(String[] args) {
        SpringApplication.run(AppRoot.class, args);
    }

    /**
     * 文件上传大小
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("50MB"); //KB,MB
        // 设置总上传数据总大小
        factory.setMaxRequestSize("200MB");
        return factory.createMultipartConfig();
    }


}
