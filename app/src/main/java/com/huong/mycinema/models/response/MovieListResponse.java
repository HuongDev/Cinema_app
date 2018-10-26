package com.huong.mycinema.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huong.mycinema.models.MovieListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MovieListResponse {
    @SerializedName("films")
    @Expose
    private List<MovieListData> data = new ArrayList<MovieListData>();

    public List<MovieListData> getData() {
        return data;
    }

    public void setData(List<MovieListData> data) {
        this.data = data;
    }
}
