package com.xinaml.frame.common.init;

import com.xinaml.frame.base.dto.RT;
import com.xinaml.frame.common.utils.PassWordUtil;
import com.xinaml.frame.dto.UserDTO;
import com.xinaml.frame.entity.User;
import com.xinaml.frame.ser.UserSer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
public class InitSysUser {
    private static Logger LOGGER = LoggerFactory.getLogger(InitSysUser.class);
    @Autowired
    private UserSer userSer;

    @PostConstruct
    @Transactional
    public  void init()throws Exception{
        UserDTO dto = new UserDTO();
        dto.addRT(RT.eq("username","admin"));
        if(null==userSer.findOne(dto)){
            User u = new User();
            u.setEmail("xinaml@163.com");
            u.setPhone("199999999");
            u.setUsername("admin");
            u.setPassword(PassWordUtil.genSaltPwd("123456"));
            u.setCreateDate(LocalDateTime.now());
            userSer.save(u);
            LOGGER.info("初始化Admin用户...");
        }
    }
}
