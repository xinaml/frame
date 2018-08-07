package com.xinaml.frame.common.custom.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;
@ApiModel(value = "result")
public class MapResult<T> {
    @ApiModelProperty("数据行")
    private List<T> rows;
    @ApiModelProperty("数据量")
    private Long total;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public MapResult() {

    }

    public MapResult(Map<String, Object> maps) {
        MapResult rs = new MapResult<T>();
        rs.setRows((List<T>) maps.get("rows"));
        rs.setTotal((Long) maps.get("total"));
    }

}
