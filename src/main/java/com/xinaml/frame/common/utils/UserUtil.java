package com.xinaml.frame.common.utils;

import com.xinaml.frame.common.constant.FinalConstant;
import com.xinaml.frame.common.session.UserSession;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * test
 */
public class UserUtil {

    public static boolean isLogin(String token) {
        if (StringUtils.isNotBlank(token) && null!=(UserSession.get(token))) {
            return  true;
        }
        return false;
    }

    public static boolean isLogin(HttpServletRequest request) {
        String token = request.getHeader(FinalConstant.TOKEN);
        if (StringUtils.isNotBlank(token) && null!=(UserSession.get(token))) {
            return  true;
        }
        return false;
    }

}
