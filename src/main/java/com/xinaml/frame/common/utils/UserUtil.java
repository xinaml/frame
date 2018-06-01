package com.xinaml.frame.common.utils;

import com.xinaml.frame.common.constant.FinalConstant;
import com.xinaml.frame.common.session.UserSession;
import com.xinaml.frame.entity.User;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * test
 */
public class UserUtil {

    public static boolean isLogin(String token) {
        return StringUtils.isNotBlank(token);
    }

    public static boolean isLogin(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotBlank(token) && null != (UserSession.get(token))) {
            return true;
        }
        return false;
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(FinalConstant.TOKEN); //取header的token
        if (StringUtils.isBlank(token)) { //取cookie的token
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(FinalConstant.TOKEN)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    public static User getUser() {
        HttpServletRequest request = RequestUtil.get();
        String token = getToken(request);
        if (StringUtils.isNotBlank(token)) {
            return UserSession.get(token);
        }
        return null;
    }

    public static User getUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            return UserSession.get(token);
        }
        return null;
    }


}
