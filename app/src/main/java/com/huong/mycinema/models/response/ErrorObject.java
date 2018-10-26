package com.huong.mycinema.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongPN on 10/24/2018.
 */
public class ErrorObject {
    @SerializedName("status")
    private Integer status;
    @SerializedName("errorMessage")
    private String errorMessage;
    @SerializedName("error")
    private String error;
    @SerializedName("message")
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
