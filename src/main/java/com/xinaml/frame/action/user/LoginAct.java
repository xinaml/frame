package com.xinaml.frame.action.user;

import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.constant.FinalConstant;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.utils.IpUtil;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.LoginTO;
import org.apache.commons.lang3.StringUtils;
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

        if (UserUtil.isLogin(request)) {
            return "redirect:/";
        } else {
            String url = request.getHeader("Referer");
            if (StringUtils.isNotBlank(url)) {
                url = StringUtils.substringAfterLast(url, "/").equals("login") ? "/" : url;
            } else {
                url = "/";
            }
            model.addAttribute("prevUrl", url);//上一个未登陆页
            return "user/login";
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public Result login(@Validated(ADD.class) LoginTO to, HttpServletRequest request,HttpServletResponse response) throws ActException {
        try {
            to.setIp(IpUtil.getIp(request));
            String token = userSer.login(to);
            Map<String, String> maps = new HashMap<>(1);
            maps.put(FinalConstant.TOKEN, token);
            Cookie cookie = new Cookie(FinalConstant.TOKEN,token);
            cookie.setMaxAge(60*60*24);
            response.addCookie(cookie);
            return new ActResult(FinalConstant.SUCCESS, maps);

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout( HttpServletRequest request,HttpServletResponse response) throws ActException {
        try {
            String token = UserUtil.getToken(request);
            boolean isOut = userSer.logout(token);
            if(isOut){
                Cookie cookie = new Cookie(FinalConstant.TOKEN,token);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return "redirect:/login";

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

}
