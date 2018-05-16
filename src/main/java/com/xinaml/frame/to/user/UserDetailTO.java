package com.xinaml.frame.to.user;

import com.xinaml.frame.base.to.BaseTO;
import com.xinaml.frame.entity.User;
import java.time.LocalTime;
import java.time.LocalDate;

/**
 * 用户详情添加修改实体传输对象
 *
 * @Author:	[lgq]
 * @Date: [2018-05-16 16:54:18]
 * @Description: [用户详情]
 * @Version: [0.0.1]
 * @Copy: [com.xinaml.frame]
 */
public class UserDetailTO extends BaseTO{

    /**
      * 所属用户
      */
    private User user;
    /**
      * 住址
      */
    private String address;
    /**
      * 身高
      */
    private Integer height;
    /**
      * 是否已婚
      */
    private Boolean married;
    /**
      * 资产
      */
    private Double asset;
    /**
      * 时间
      */
    private LocalTime time;
    /**
      * 日期
      */
    private LocalDate date;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public Double getAsset() {
        return asset;
    }

    public void setAsset(Double asset) {
        this.asset = asset;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }



}

