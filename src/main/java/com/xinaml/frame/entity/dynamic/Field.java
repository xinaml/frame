package com.xinaml.frame.entity.dynamic;


import com.xinaml.frame.types.FieldType;

/**
 * mongo 动态表操作 列值
 */
public class Field {
    private String name;
    private String val;
    private FieldType type;

    public Field(String name, String val,FieldType type) {
        this.name = name;
        this.val = val;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}
