package com.xinaml.frame.common.utils;

import com.xinaml.frame.common.constant.FinalConstant;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {

    public static boolean isLogin(String token){
        return true;
    }

    public static boolean isLogin(HttpServletRequest request){
        String token = request.getHeader(FinalConstant.TOKEN);
        return true;
    }
}
