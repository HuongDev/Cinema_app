package com.huong.mycinema.ui.login;

import com.google.gson.JsonObject;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class LoginRequest {
    private String mail;
    private String password;

    public LoginRequest(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JsonObject toJsonObject() {
        JsonObject body = new JsonObject();
        body.addProperty("email", getMail());
        body.addProperty("password", getPassword());
        return body;
    }
}
