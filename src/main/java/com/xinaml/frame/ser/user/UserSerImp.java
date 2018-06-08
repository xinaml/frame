package com.xinaml.frame.ser.user;

import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.base.service.ServiceImpl;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.session.UserSession;
import com.xinaml.frame.common.utils.PassWordUtil;
import com.xinaml.frame.common.utils.TokenUtil;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;
import com.xinaml.frame.to.user.LoginTO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

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
            UserSession.put(token,user);
            return token;
        } else {
            throw new SerException("账号或密码错误");
        }
    }

    @Override
    public Boolean logout(String token) throws SerException {
        if (StringUtils.isNotBlank(token)) {
            UserSession.remove(token);
        }
        return true;
    }

    @Cacheable(value="users",key = "#dto.serId")
    @Override
    public Map<String, Object> findByPage(UserDTO dto) throws SerException {
        return super.findByPage(dto);
    }

}
