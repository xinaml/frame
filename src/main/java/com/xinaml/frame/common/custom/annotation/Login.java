package com.xinaml.frame.common.custom.annotation;


import java.lang.annotation.*;

/**
 * 自定义注解
 * 登录拦截标识
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
