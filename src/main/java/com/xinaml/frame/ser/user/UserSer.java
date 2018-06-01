package com.xinaml.frame.ser.user;

import com.xinaml.frame.base.service.Ser;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;
import com.xinaml.frame.to.LoginTO;

public interface UserSer extends Ser<User,UserDTO> {

     String login(LoginTO loginTO)throws SerException;
     Boolean logout(String token)throws SerException;
}
