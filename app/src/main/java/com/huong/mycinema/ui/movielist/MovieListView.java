package com.huong.mycinema.ui.movielist;

import com.huong.mycinema.models.response.MovieListResponse;

/**
 * Created by HuongPN on 10/17/2018.
 */
interface MovieListView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void getMovieListSuccess(MovieListResponse cityListResponse);
}
