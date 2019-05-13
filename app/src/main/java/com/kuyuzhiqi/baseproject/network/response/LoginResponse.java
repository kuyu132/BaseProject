package com.kuyuzhiqi.baseproject.network.response;


import com.kuyuzhiqi.baseproject.db.entity.User;
import com.kuyuzhiqi.network.reponse.BaseBizResponse;

public class LoginResponse extends BaseBizResponse {
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
