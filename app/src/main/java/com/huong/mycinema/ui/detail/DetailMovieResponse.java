package com.huong.mycinema.ui.detail;

import com.google.gson.annotations.SerializedName;
import com.huong.mycinema.models.MovieListData;

/**
 * Created by HuongPN on 10/19/2018.
 */
public class DetailMovieResponse {
    @SerializedName("cinema")
    private MovieListData data;

    public MovieListData getData() {
        return data;
    }

    public void setData(MovieListData data) {
        this.data = data;
    }
}
