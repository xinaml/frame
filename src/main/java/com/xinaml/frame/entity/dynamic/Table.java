package com.xinaml.frame.entity.dynamic;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

/**
 * mongo 动态表操作 表
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Table {
    @Id
    private String id;
    private String name;
    private LocalDateTime createTime;
    private List<FieldConf> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldConf> getFields() {
        return fields;
    }

    public void setFields(List<FieldConf> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
