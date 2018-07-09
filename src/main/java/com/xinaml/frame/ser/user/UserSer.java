package com.xinaml.frame.ser.user;

import com.xinaml.frame.base.ser.Ser;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;
import com.xinaml.frame.to.user.LoginTO;
import com.xinaml.frame.to.user.RegisterTO;

public interface UserSer extends Ser<User, UserDTO> {
    /**
     * 登录
     * @param to
     * @return
     * @throws SerException
     */
    String login(LoginTO to) throws SerException;
    /**
     * 注册
     * @param to
     * @return
     * @throws SerException
     */
    String register(RegisterTO to) throws SerException;
    /**
     * 退出
     * @param token
     * @return
     * @throws SerException
     */
    Boolean logout(String token) throws SerException;

}
