package com.xinaml.frame.common.session;

import com.google.common.cache.*;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UserSession {
    private static Logger LOGGER = LoggerFactory.getLogger(UserSession.class);
    private static final RuntimeException TOKEN_NOT_NULL = new RuntimeException("token令牌不能为空");

    private static final LoadingCache<String, User> AUTH_CODE_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.HOURS)
            .maximumSize(1000)
            .removalListener(new RemovalListener<String, User>() {
                @Override
                public void onRemoval(RemovalNotification<String, User> notification) {
                    LOGGER.info("remove:" + notification.getCause().name());
                }
            })
            .build(new CacheLoader<String, User>() {
                @Override
                public User load(String key) throws Exception {
                    return null;
                }
            });

    public static void put(String token, User user) throws SerException {
        if (StringUtils.isNotBlank(token)) {
            AUTH_CODE_SESSION.put(token, user);
        } else {
            throw TOKEN_NOT_NULL;
        }
    }


    public static void remove(String token) {
        if (StringUtils.isNotBlank(token)) {
            AUTH_CODE_SESSION.invalidate(token);
        } else {

            throw TOKEN_NOT_NULL;
        }
    }


    public static User get(String token) {
        try {
            if (StringUtils.isNotBlank(token)) {
                return AUTH_CODE_SESSION.get(token);
            }
        } catch (Exception e) {
            return null;
        }

        throw TOKEN_NOT_NULL;
    }

}
