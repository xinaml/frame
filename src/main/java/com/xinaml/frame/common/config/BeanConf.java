package com.xinaml.frame.common.config;

import com.xinaml.frame.base.rep.RedisRep;
import com.xinaml.frame.common.utils.UserUtil;
import com.xinaml.frame.ser.user.UserSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanConf {
    @Autowired
    public UserSer userSer;

    @Autowired
    private RedisRep redisRep;

    @PostConstruct
    public void init() {
        UserUtil.userSer = userSer;
        UserUtil.redisRep = redisRep;
    }
}
