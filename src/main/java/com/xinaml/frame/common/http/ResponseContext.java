package com.xinaml.frame.common.http;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 写入数据到页面
 *
 * @author lgq
 * @date 2018/4/15
 **/
public class ResponseContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseContext.class);

    private ResponseContext() {
    }

    public static HttpServletResponse get() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();//springmvc 自带
    }

    private static HttpServletResponse init() {
        HttpServletResponse response = get();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        return response;
    }


    public static void writeData(Object data) {
        try {
            init().getWriter().print(JSON.toJSON(data));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void writeData(HttpServletResponse response, Object data) {
        try {
            init().getWriter().print(JSON.toJSON(data));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
