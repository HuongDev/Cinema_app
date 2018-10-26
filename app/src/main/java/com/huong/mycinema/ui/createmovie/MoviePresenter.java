package com.huong.mycinema.ui.createmovie;

import com.huong.mycinema.networking.Service;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MoviePresenter {
    private final Service service;
    private final MovieView view;
    private CompositeSubscription subscriptions;

    public MoviePresenter(Service service, MovieView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

//    public void postMovie(HashMap<String, RequestBody> hashMap, MultipartBody.Part file) {
//        view.showWait();
//
//        Subscription subscription = service.uploadMovie(new Service.ListCallback() {
//            @Override
//            public void onSuccess(CinemaResponse cityListResponse) {
//                view.removeWait();
//                view.getFilmListSuccess(cityListResponse);
//            }
//
//            @Override
//            public void onError(NetworkError networkError) {
//                view.removeWait();
//                view.onFailure(networkError.getAppErrorMessage());
//            }
//
//        }, hashMap, file);
//
//        subscriptions.add(subscription);
//    }


    public void onStop() {
        subscriptions.unsubscribe();
    }
}
