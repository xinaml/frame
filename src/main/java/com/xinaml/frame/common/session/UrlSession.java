package com.xinaml.frame.common.session;

import com.google.common.cache.*;
import com.xinaml.frame.common.custom.exception.SerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UrlSession {
    private static Logger LOGGER = LoggerFactory.getLogger(UrlSession.class);

    private static final LoadingCache<String, String> AUTH_CODE_SESSION = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
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

    public static void put(String sid, String url) throws SerException {
        if (StringUtils.isNotBlank(sid)) {
            AUTH_CODE_SESSION.put(sid, url);
        }
    } public static String get(String sid) throws SerException {
        if (StringUtils.isNotBlank(sid)) {
            try {
                return AUTH_CODE_SESSION.get(sid );
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void remove(String sid) {
        if (StringUtils.isNotBlank(sid)) {
            AUTH_CODE_SESSION.invalidate(sid);
        }
    }


}
