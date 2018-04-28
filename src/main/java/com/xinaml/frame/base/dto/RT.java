/**
 * 条件构建
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.base.dto;

import java.io.Serializable;

public class RT implements Serializable {
    /**
     * field（字段） 包含 "." 则默认会设置成左连接，左连接set 集合 命名必须未Set结束
     * 如：Set<User>userSet List<User>userList
     */
    private String field; //字段
    private Object value;//字段值
    private RestrictType restrictType; //限制表达式

    private RT() {
    }

    public RT(String field, Object value, RestrictType restrictType) {
        this.field = field;
        this.value = value;
        this.restrictType = restrictType;
    }

    public static Restrict eq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.EQ);
    }

    public static Restrict between(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.BETWEEN);
    }

    public static Restrict like(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LIKE);
    }

    public static Restrict in(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.IN);
    }

    public static Restrict gt(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.GT);
    }

    public static Restrict lt(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LT);
    }

    public static Restrict gt_eq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.GTEQ);
    }

    public static Restrict lt_eq(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.LTEQ);
    }

    public static Restrict or(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.OR);
    }

    public static Restrict ne(String filed, Object value) {
        return new Restrict(filed, value, RestrictType.NE);
    }

    public static Restrict isNull(String filed) {
        return new Restrict(filed, null, RestrictType.ISNULL);
    }

    public static Restrict isNotNull(String filed) {
        return new Restrict(filed, null, RestrictType.ISNOTNULL);
    }

    public static Restrict notIn(String field, Object[] values) {
        return new Restrict(field, values, RestrictType.NOTIN);
    }
}
