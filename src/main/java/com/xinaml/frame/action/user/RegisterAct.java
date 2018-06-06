package com.xinaml.frame.action.user;

import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.to.user.RegisterTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterAct {

    @GetMapping("/register")
    public String register() throws ActException {
            return "user/register";//跳转注册页
    }

    @ResponseBody
    @PostMapping("/register")
    public String register(RegisterTO to) throws ActException {
        return "success";
    }
}
