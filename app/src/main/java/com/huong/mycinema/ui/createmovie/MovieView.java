package com.huong.mycinema.ui.createmovie;

import com.huong.mycinema.models.response.CinemaResponse;
import com.huong.mycinema.models.response.MovieListResponse;

/**
 * Created by HuongPN on 10/17/2018.
 */
public interface MovieView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void getCityListSuccess(MovieListResponse cityListResponse);

    void getFilmListSuccess(CinemaResponse movieListResponse);
}
