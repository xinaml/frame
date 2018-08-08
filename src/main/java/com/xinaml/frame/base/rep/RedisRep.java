package com.xinaml.frame.base.rep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisRep {
    @Autowired
    private StringRedisTemplate template;

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, value);
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     */
    public void put(String key, String value, long timeOut, TimeUnit timeUnit) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, value, timeOut, timeUnit);
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    /**
     * 设置过期
     *
     * @param key
     * @param timeOut
     * @param timeUnit
     */
    public void expire(String key, long timeOut, TimeUnit timeUnit) {
        this.template.expire(key, timeOut, timeUnit);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit
     */
    public void getExpire(String key, TimeUnit timeUnit) {
        long time = this.template.getExpire(key, timeUnit);
    }

    /**
     * 删除建
     *
     * @param key
     */
    public void del(String key) {
        this.template.delete(key);
    }

    /**
     * 是否存在
     *
     * @param key
     */
    public boolean exists(String key) {
        return this.template.hasKey(key);
    }

    /**
     * 保存集合
     *
     * @param key
     * @param value
     */
    public void setHashSet(String key, String... value) {
        template.opsForSet().add(key, value);
    }

    /**
     * 根据key获取集合
     *
     * @param key
     * @return
     */
    public Set<String> getHashSet(String key) {
        return template.opsForSet().members(key);
    }

    /**
     * 根据key查看集合中是否存在指定数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean existsHashSet(String key, String value) {
        return template.opsForSet().isMember(key, value);
    }

}
