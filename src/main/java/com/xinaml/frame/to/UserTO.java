package com.xinaml.frame.to;

import com.xinaml.frame.base.entity.ADD;
import com.xinaml.frame.base.entity.DEL;
import com.xinaml.frame.base.entity.EDIT;
import com.xinaml.frame.base.entity.GET;
import com.xinaml.frame.base.to.BaseTO;
import com.xinaml.frame.common.custom.constant.CommonConst;
import com.xinaml.frame.types.SexType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserTO extends BaseTO{
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

    @DateTimeFormat(pattern = CommonConst.DATE)  //格式化接收前端传过来的日期
    private LocalDate birthday;

    @DateTimeFormat(pattern = CommonConst.DATETIME)//格式化接收前端传过来的日期时间
    private LocalDateTime createTime;

    private Integer age;

    private Double weight;

    private SexType sexType;

    private Boolean expired;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public SexType getSexType() {
        return sexType;
    }

    public void setSexType(SexType sexType) {
        this.sexType = sexType;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

