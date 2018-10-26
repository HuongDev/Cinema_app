package com.huong.mycinema.networking;

import com.google.gson.JsonObject;
import com.huong.mycinema.ui.login.LoginResponse;
import com.huong.mycinema.models.response.MovieListResponse;
import com.huong.mycinema.models.response.ResponseData;
import com.huong.mycinema.ui.detail.DetailMovieResponse;
import com.huong.mycinema.ui.profile.UserInfoResponse;
import com.huong.mycinema.ui.signup.SignUpResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by HuongPN on 10/17/2018.
 */
public interface NetworkService {
    @GET("api/cinema")
    Observable<MovieListResponse> getMovieList();

    @Multipart
    @POST("api/cinema")
    Call<ResponseBody> uploadMovie(@PartMap HashMap<String, RequestBody> hashMap, @Part MultipartBody.Part file);

    @POST("api/auth/signup")
    Call<SignUpResponse> postSignUp(@Body JsonObject jsonObject);

    @POST("api/auth/signin")
    Call<LoginResponse> postSignIn(@Body JsonObject jsonObject);

    @GET("api/cinema/{id}")
    Call<DetailMovieResponse> getDetailMovie(@Path("id") String postsId);

    @POST("api/user/change-password")
    Call<ResponseData> getChangePassword(@Header("x-access-token") String token, @Body JsonObject jsonObject);

    @POST("api/auth/reset-password")
    Call<JsonObject> resetPassword(@Header("token") String token, @Body JsonObject jsonObject);

    @POST("api/user/edit")
    Call<JsonObject> updateName(@Header("x-access-token") String token, @Body JsonObject jsonObject);

    @Multipart
    @POST("api/user/change-avatar")
    Call<JsonObject> changeAvatar(@Header("x-access-token") String token, @Part MultipartBody.Part file);

    @GET("api/cinema")
    Call<MovieListResponse> getMyMovie();

    @POST("api/auth/user")
    Call<UserInfoResponse> getUserInfo(@Body JsonObject jsonObject);

    @POST("api/cinema/delete")
    Call<JsonObject> deleteMovie(@Header("x-access-token") String token, @Body JsonObject jsonObject);

    @Multipart
    @POST("api/cinema/edit")
    Call<JsonObject> editMovie(@Header("x-access-token") String token, @PartMap HashMap<String, RequestBody> hashMap, @Part MultipartBody.Part file);
}
