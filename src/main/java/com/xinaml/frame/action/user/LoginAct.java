package com.xinaml.frame.action.user;

import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.constant.FinalConstant;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.session.UrlSession;
import com.xinaml.frame.common.utils.IpUtil;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.LoginTO;
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

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) throws ActException {
        try {
            if (UserUtil.isLogin(request)) {
                String url = UrlSession.get(request.getSession().getId());
                if(null!=url){
                    return "redirect:"+url;
                }
                return "redirect:/";
            } else {

                String url = UrlSession.get(request.getSession().getId());
                model.addAttribute("prevUrl", url);
                return "user/login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "user/login";
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public Result login(@Validated(ADD.class) LoginTO to, HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            to.setIp(IpUtil.getIp(request));
            String token = userSer.login(to);
            Map<String, String> maps = new HashMap<>(1);
            maps.put(FinalConstant.TOKEN, token);
            Cookie cookie = new Cookie(FinalConstant.TOKEN, token);
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
            UrlSession.remove(request.getSession().getId());
            return new ActResult(FinalConstant.SUCCESS, maps);

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ActException {
        try {
            String token = UserUtil.getToken(request);
            boolean isOut = userSer.logout(token);
            if (isOut) {
                Cookie cookie = new Cookie(FinalConstant.TOKEN, token);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/login";

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

}
