package com.kuyuzhiqi.baseproject.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    private Long id;
    private String user_name;
    private String email;
    private String mobile;
    private Long update_at;
    private Long delete_at;

    @Generated(hash = 395294484)
    public User(Long id, String user_name, String email, String mobile,
            Long update_at, Long delete_at) {
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.mobile = mobile;
        this.update_at = update_at;
        this.delete_at = delete_at;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Long update_at) {
        this.update_at = update_at;
    }

    public Long getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(Long delete_at) {
        this.delete_at = delete_at;
    }
}
