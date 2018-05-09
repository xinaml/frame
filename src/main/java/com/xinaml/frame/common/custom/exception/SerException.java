/**
 * 业务层 自定义异常
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.common.custom.exception;

public class SerException extends Exception {
    public SerException(String msg) {
        super(msg);
    }

    public SerException() {
        super();
    }

    public SerException(String msg, int code) {
        super(code + "@" + msg);
    }
}
