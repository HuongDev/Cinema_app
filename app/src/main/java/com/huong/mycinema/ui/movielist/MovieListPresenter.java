package com.huong.mycinema.ui.movielist;

import com.huong.mycinema.models.response.MovieListResponse;
import com.huong.mycinema.networking.NetworkError;
import com.huong.mycinema.networking.Service;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MovieListPresenter {
    private final Service service;
    private final MovieListView view;
    private CompositeSubscription subscriptions;

    public MovieListPresenter(Service service, MovieListView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getMovieList() {
        view.showWait();

        Subscription subscription = service.getMovieList(new Service.GetMovieListCallback() {
            @Override
            public void onSuccess(MovieListResponse movieListResponse) {
                view.removeWait();
                view.getMovieListSuccess(movieListResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
