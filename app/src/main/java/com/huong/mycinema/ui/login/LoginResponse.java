package com.huong.mycinema.ui.login;

import com.google.gson.annotations.SerializedName;
import com.huong.mycinema.ui.signup.User;

import java.io.Serializable;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class LoginResponse implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("user")
    private User user;
    @SerializedName("token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
