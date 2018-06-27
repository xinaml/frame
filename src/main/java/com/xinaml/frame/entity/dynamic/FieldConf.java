package com.xinaml.frame.entity.dynamic;


import com.xinaml.frame.types.FieldType;

/**
 * mongo 动态表操作 列配置
 */
public class FieldConf {
    private String name;
    private FieldType type;
    private Integer seq;

    public FieldConf(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}
