/**
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.base.dto;

import java.io.Serializable;

/**
 * 条件
 *
 * @author lgq
 */
public class Restrict implements Serializable {

    private String field; //字段
    private Object value;//字段值
    private RestrictType restrictType; //限制表达式

    private Restrict() {
    }

    public Restrict(String field, Object value, RestrictType restrictType) {
        this.field = field;
        this.value = value;
        this.restrictType = restrictType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public RestrictType getRestrictType() {
        return restrictType;
    }

    public void setRestrictType(RestrictType restrictType) {
        this.restrictType = restrictType;
    }
}
