package com.xinaml.frame.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final String REG = "[\u4e00-\u9fa5]";

    /**
     * 是否包含汉字
     * @param str
     * @return
     */
    public static  boolean isChinese(String str) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
            Pattern p = Pattern.compile(REG);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
}
