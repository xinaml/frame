package com.xinaml.frame.ser;

import com.xinaml.frame.base.service.ServiceImpl;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserSerImp extends ServiceImpl<User, UserDTO> implements UserSer {
}
