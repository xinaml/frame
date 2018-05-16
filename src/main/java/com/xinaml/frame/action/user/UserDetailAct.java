package com.xinaml.frame.action.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.xinaml.frame.dto.user.UserDetailDTO;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.ser.user.UserDetailSer;

/**
 * 用户详情控制器
 *
 * @Author:	[lgq]
 * @Date: [2018-05-16 16:54:18]
 * @Description: [用户详情]
 * @Version: [0.0.1]
 * @Copy: [com.xinaml.frame]
 */
@Controller
public class UserDetailAct {

    @Autowired
    private UserDetailSer userDetailSer;

    @ResponseBody
    @GetMapping("maps")
    public String maps(UserDetailDTO dto) throws ActException {
        try {
            Map<String, Object> maps = userDetailSer.findByPage(dto);
            return JSON.toJSONString(maps);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }
}
