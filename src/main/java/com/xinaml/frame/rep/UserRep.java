/**
 * 用户持久化接口
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.rep;

import com.xinaml.frame.base.rep.JapRep;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;

public interface UserRep extends JapRep<User, UserDTO> {
}
