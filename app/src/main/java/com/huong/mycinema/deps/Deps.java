package com.huong.mycinema.deps;

import com.huong.mycinema.ui.createmovie.CreateMovieActivity;
import com.huong.mycinema.ui.login.LoginActivity;
import com.huong.mycinema.networking.NetworkModule;
import com.huong.mycinema.ui.changepass.ChangePasswordActivity;
import com.huong.mycinema.ui.detail.DetailMovieActivity;
import com.huong.mycinema.ui.editMovie.EditMovieActivity;
import com.huong.mycinema.ui.movielist.MovieListActivity;
import com.huong.mycinema.ui.profile.ProfileActivity;
import com.huong.mycinema.ui.signup.SignUpActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by HuongPN on 10/17/2018.
 */
@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void inject(CreateMovieActivity createMovieActivity);

    void inject(MovieListActivity movieListActivity);

    void inject(LoginActivity LoginActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(DetailMovieActivity detailMovieActivity);

    void inject(ProfileActivity profileActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(EditMovieActivity editMovieActivity);
}
