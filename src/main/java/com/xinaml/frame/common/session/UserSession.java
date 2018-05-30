package com.xinaml.frame.common.session;

import com.google.common.cache.*;
import com.xinaml.frame.common.custom.exception.SerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UserSession {
    private static Logger LOGGER = LoggerFactory.getLogger(UserSession.class);
    private static final RuntimeException TOKEN_NOT_NULL = new RuntimeException("token令牌不能为空");

    private static final LoadingCache<String, String> AUTH_CODE_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.HOURS)
            .maximumSize(1000)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    LOGGER.info("remove:" + notification.getCause().name());
                }
            })
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return null;
                }
            });

    public static void put(String account, String code) throws SerException {
        if (StringUtils.isNotBlank(account)) {
            AUTH_CODE_SESSION.put(account, code);
        } else {
            throw TOKEN_NOT_NULL;
        }
    }


    public static void remove(String account) {
        if (StringUtils.isNotBlank(account)) {
            AUTH_CODE_SESSION.invalidate(account);
        } else {

            throw TOKEN_NOT_NULL;
        }
    }


    public static String get(String account) {
        try {
            if (StringUtils.isNotBlank(account)) {
                return AUTH_CODE_SESSION.get(account);
            }
        } catch (Exception e) {
            return null;
        }

        throw TOKEN_NOT_NULL;
    }

}
