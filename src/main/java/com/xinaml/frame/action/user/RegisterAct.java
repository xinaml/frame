package com.xinaml.frame.action.user;

import com.xinaml.frame.base.atction.BaseAct;
import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.ser.user.UserSer;
import com.xinaml.frame.to.user.RegisterTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RegisterAct extends BaseAct {
    @Autowired
    private UserSer userSer;

    @GetMapping("/register")
    public ModelAndView register() throws ActException {
        return new ModelAndView("/register");//跳转注册页
    }

    @PostMapping("/register")
    public Result register(@Validated(ADD.class) RegisterTO to) throws ActException {
        try {
            userSer.register(to);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
        return new ActResult(SUCCESS);

    }
}
