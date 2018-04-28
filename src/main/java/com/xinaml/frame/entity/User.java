/**
 * 用户实体
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.entity;

import com.xinaml.frame.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(25) COMMENT '用户名'")
    private String username;

    @Column(unique = true, nullable = false, length = 20, columnDefinition = "VARCHAR(25) COMMENT '邮件'")
    private String email;

    @Column(unique = true, nullable = false, length = 20, columnDefinition = "VARCHAR(25) COMMENT '手机号'")
    private String phone;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
