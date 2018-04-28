package com.xinaml.frame.to;

import com.xinaml.frame.common.aspect.ADD;
import com.xinaml.frame.common.aspect.DEL;
import com.xinaml.frame.common.aspect.EDIT;
import com.xinaml.frame.common.aspect.GET;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserTO {
    @NotBlank(groups = {ADD.class, EDIT.class}, message = "用户名不能为空！")
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;

    @NotBlank(groups = {ADD.class, EDIT.class}, message = "手机不能为空！")
    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    private String phone;

    @ApiModelProperty(value = "邮箱", name = "email", required = true)
    @Email
    @NotBlank(groups = {ADD.class, EDIT.class}, message = "邮箱不能为空！")
    private String email;
    @NotBlank(groups = {GET.class, DEL.class, EDIT.class}, message = "id不能为空！")
    private String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
