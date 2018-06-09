package com.xinaml.frame.action.user;

import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.constant.FinalConst;
import com.xinaml.frame.common.constant.PathConst;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.utils.IpUtil;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.user.LoginTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginAct {
    @Autowired
    private UserSer userSer;

    /**
     * 转登录页
     *
     * @param model
     * @param request
     * @return
     * @throws ActException
     */
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) throws ActException {
        try {
            Object prevUrl = request.getSession().getAttribute(PathConst.PREV_URL);//获取上次请求页
            if (UserUtil.isLogin(request)) {
                if (null != prevUrl) {
                    return "redirect:" + prevUrl;
                }
                return "redirect:/";
            } else {
                model.addAttribute(PathConst.PREV_URL, prevUrl);
                return "user/login"; //跳转登录页
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "user/login";//跳转登录页
        }
    }

    /**
     * 用户登录
     *
     * @param to
     * @param request
     * @param response
     * @return
     * @throws ActException
     */
    @ResponseBody
    @PostMapping("/login")
    public Result login(@Validated(ADD.class) LoginTO to, HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            to.setIp(IpUtil.getIp(request));
            String token = userSer.login(to);
            Map<String, String> maps = new HashMap<>(1);
            maps.put(FinalConst.TOKEN, token);
            Cookie cookie = new Cookie(FinalConst.TOKEN, token);
            cookie.setMaxAge(60 * 60 * 24); //token 有效期为一天
            response.addCookie(cookie);
            request.getSession().invalidate();
            return new ActResult(FinalConst.SUCCESS, maps);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     * @throws ActException
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            String token = UserUtil.getToken(request);
            boolean isOut = userSer.logout(token);
            if (isOut) {
                Cookie cookie = new Cookie(FinalConst.TOKEN, token);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/login";

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

}
