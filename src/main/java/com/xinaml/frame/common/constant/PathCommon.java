package com.xinaml.frame.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PathCommon {
    @Value("${storage.path}")
    private String rootPath;
    @PostConstruct
    public void initPath(){
        PathCommon.ROOT_PATH=rootPath;
    }
    public static final String SEPARATOR=System.getProperty("file.separator");
    public static String ROOT_PATH;
    public static final String TMP_PATH ="/tmp";


}
