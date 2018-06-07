package com.xinaml.frame.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisRep {
    @Autowired
    private Jedis jedis;

    public String get(String key) {
        return jedis.get(key);
    }

    public void set(String key, String val) {
        jedis.set(key, val);
    }

    public void del(String... key) {
        jedis.del(key);
    }

    public String getSet(String key, String val) {
        return jedis.getSet(key, val);
    }

    public void setWithExpireTime(String key, String value, int expireTime) {
        jedis.setex(key, expireTime, value);
    }

}
