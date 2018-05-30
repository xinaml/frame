package com.xinaml.frame.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.common.constant.FinalConstant;
import com.xinaml.frame.common.custom.annotation.Login;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登录拦截
 *
 * @author lgq
 * @date 2018/4/15
 **/
@Component
public class LoginIntercept extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true; //不拦截未知 action
        }
        Method method = ((HandlerMethod) handler).getMethod();
        Class<?> clazz = method.getDeclaringClass();
        //该类或者方法上是否有登录安全认证注解
        if (clazz.isAnnotationPresent(Login.class) || method.isAnnotationPresent(Login.class)) {
            return validateLogin(request, response);
        }
        return true;

    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //调用完UserFilter才会调用此方法
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


    private boolean validateLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader(FinalConstant.TOKEN);
            if (StringUtils.isNotBlank(token)) {
                return true;  // 查询是否已登录
            } else {
                ActResult result = new ActResult();
                result.setCode(403);
                result.setMsg("请先登录!");
                response.setStatus(200);
                ResponseUtil.writeData(JSON.toJSONString(result));
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
