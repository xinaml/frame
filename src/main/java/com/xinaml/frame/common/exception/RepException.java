/**
 * rep层 自定义异常
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.common.exception;

import com.xinaml.frame.common.types.RepExceptionType;

public class RepException extends Exception {

    private RepExceptionType type = RepExceptionType.UNDEFINE;

    private RepException repException;

    public RepException(RepExceptionType repExceptionType, String msg) {
        super(msg);
        this.type = repExceptionType;
    }

    public RepException(String msg) {
        super(msg);
    }

    public RepException getRepException() {
        return repException;
    }

    public void setRepException(RepException repException) {
        this.repException = repException;
    }

    public RepExceptionType getType() {
        return type;
    }

    public void setType(RepExceptionType type) {
        this.type = type;
    }
}
