/**
 * 用户持久化接口
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.rep.user;

import com.xinaml.frame.base.rep.JapRep;
import com.xinaml.frame.dto.user.UserDTO;
import com.xinaml.frame.entity.user.User;

public interface UserRep extends JapRep<User, UserDTO> {
    User findByUsername(String username);
}
