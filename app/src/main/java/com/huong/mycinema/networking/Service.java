package com.huong.mycinema.networking;

import com.huong.mycinema.models.response.CinemaResponse;
import com.huong.mycinema.models.response.MovieListResponse;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription getMovieList(final GetMovieListCallback callback) {

        return networkService.getMovieList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MovieListResponse>>() {
                    @Override
                    public Observable<? extends MovieListResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<MovieListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(MovieListResponse cityListResponse) {
                        callback.onSuccess(cityListResponse);

                    }
                });
    }

//    public Subscription uploadMovie(final ListCallback callback, HashMap<String, RequestBody> hashMap, MultipartBody.Part file) {
//
//        return networkService.uploadMovie(hashMap, file)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorResumeNext(new Func1<Throwable, Observable<? extends CinemaResponse>>() {
//                    @Override
//                    public Observable<? extends CinemaResponse> call(Throwable throwable) {
//                        return Observable.error(throwable);
//                    }
//                })
//                .subscribe(new Subscriber<CinemaResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        callback.onError(new NetworkError(e));
//
//                    }
//
//                    @Override
//                    public void onNext(CinemaResponse cityListResponse) {
//                        callback.onSuccess(cityListResponse);
//
//                    }
//                });
//    }

    public interface ListCallback{
        void onSuccess(CinemaResponse cityListResponse);

        void onError(NetworkError networkError);
    }

    public interface GetMovieListCallback{
        void onSuccess(MovieListResponse cityListResponse);

        void onError(NetworkError networkError);
    }
}
