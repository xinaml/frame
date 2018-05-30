package com.xinaml.frame.ser;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.base.service.ServiceImpl;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.session.UserSession;
import com.xinaml.frame.common.utils.PassWordUtil;
import com.xinaml.frame.common.utils.TokenUtil;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;
import com.xinaml.frame.to.LoginTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserSerImp extends ServiceImpl<User, UserDTO> implements UserSer {
    private static Logger LOGGER = LoggerFactory.getLogger(UserSerImp.class);

    @Override
    public String login(LoginTO to) throws SerException {
        UserDTO dto = new UserDTO();
        dto.addRT(RT.eq("username", to.getUsername())); //username 必须唯一
        User user = findOne(dto);
        String token = null;
        boolean isPass = false;
        if (null != user) {
            try {
                isPass = PassWordUtil.validPwd(to.getPassword(), user.getPassword());
            } catch (Exception e) {
                LOGGER.error("密码解析错误");
            }
        } else {
            throw new SerException("账号或密码错误");
        }
        if (isPass) {
            token = TokenUtil.create(to.getIp());
            UserSession.put(token, JSON.toJSONString(user));
            return token;
        } else {
            throw new SerException("账号或密码错误");
        }
    }
}
