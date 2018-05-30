package com.xinaml.frame.action.user;

import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.to.UserTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginAct {

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) throws ActException {
        if(UserUtil.isLogin(request)){
            return "redirect:/";
        }else {
            String url =request.getHeader("Referer");
            if(StringUtils.isNotBlank(url)){
                url=StringUtils.substringAfterLast(url,"/").equals("login")?"/":url;
            }else {
                url="/";
            }
            model.addAttribute("prevUrl",url);//上一个未登陆页
            return "redirect:user/login";
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public Result login(UserTO userTO, HttpServletRequest request) throws ActException {
        String token="123456";
        Map<String,String> maps = new HashMap<>(1);
        maps.put("token",token);
        return new ActResult("success",maps) ;
    }

}
