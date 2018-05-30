package com.xinaml.frame.action.user;

import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.utils.IpUtil;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.UserSer;
import com.xinaml.frame.to.LoginTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginAct {
    @Autowired
    private UserSer userSer;

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) throws ActException {
        String url = request.getHeader("Referer");
        if (StringUtils.isBlank(request.getParameter("origin")) && StringUtils.isBlank(url)) { //禁止直接从登录页进入
            return "/error/404";
        }
        if (UserUtil.isLogin(request)) {
            return "redirect:/";
        } else {
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
    public Result login(@Validated(ADD.class) LoginTO to, HttpServletRequest request) throws ActException {
        try {
            to.setIp(IpUtil.getIp(request));
            String token = userSer.login(to);
            Map<String, String> maps = new HashMap<>(1);
            maps.put("token", token);
            return new ActResult("success", maps);

        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }

    }

}
