package com.xinaml.frame.common.config;

import com.xinaml.frame.common.Interceptor.LoginIntercept;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册
 */
@Component
public class WebConf implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginIntercept()).addPathPatterns("/**");
    }
}
