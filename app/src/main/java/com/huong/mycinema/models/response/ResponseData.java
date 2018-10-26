package com.huong.mycinema.models.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HuongPN on 10/24/2018.
 */
public class ResponseData implements Serializable {

    @SerializedName("error")
    private ErrorObject error;
    @SerializedName("status")
    private Integer status = 0;
    @SerializedName("errorMessage")
    private String errorMessage;

    public ErrorObject getError() {
        return error;
    }

    public void setError(ErrorObject error) {
        this.error = error;
    }

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
}
