package com.xinaml.frame.action.user;

import com.xinaml.frame.common.custom.exception.ActException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginAct {

    @GetMapping("/login")
    public String login() throws ActException {
        return "user/login";
    }

}
