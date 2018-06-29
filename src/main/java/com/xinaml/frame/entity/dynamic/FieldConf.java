package com.xinaml.frame.entity.dynamic;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.xinaml.frame.types.FieldType;
import org.springframework.data.annotation.Id;

/**
 * mongo 动态表操作 列配置
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldConf {
    @Id
    private String id;
    private String name;
    private FieldType type;
    private Integer seq;

    public FieldConf(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
