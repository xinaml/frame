/**
 * 用户查询数据传输
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.dto.user;

import com.xinaml.frame.base.dto.BaseDTO;

public class UserDTO extends BaseDTO {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
