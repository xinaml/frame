package com.xinaml.frame.common.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 连接池监控拦截
 */
@WebFilter(filterName="druidFilter",
        urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="/static/*,*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"),// 忽略资源
        })
public class DruidFilter  extends WebStatFilter {
}
