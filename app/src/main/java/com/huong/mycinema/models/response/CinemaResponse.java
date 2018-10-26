package com.huong.mycinema.models.response;

import com.google.gson.annotations.SerializedName;
import com.huong.mycinema.models.MovieListData;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class CinemaResponse {
    @SerializedName("cinema")
    private MovieListData movieReponse;

    public MovieListData getMovieReponse() {
        return movieReponse;
    }

    public void setMovieReponse(MovieListData movieReponse) {
        this.movieReponse = movieReponse;
    }
}
