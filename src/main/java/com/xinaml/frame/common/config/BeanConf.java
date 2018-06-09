package com.xinaml.frame.common.config;

import com.xinaml.frame.common.redis.JRedis;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.user.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanConf {
    @Autowired
    public UserSer userSer;

    @Autowired
    private JRedis jRedis;

    @PostConstruct
    public void init() {
        UserUtil.userSer = userSer;
        UserUtil.jRedis = jRedis;
    }
}
