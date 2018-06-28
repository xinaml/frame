package com.xinaml.frame.entity.dynamic;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.xinaml.frame.types.FieldType;
import org.springframework.data.annotation.Id;

/**
 * mongo 动态表操作 列值
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field {
    @Id
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
