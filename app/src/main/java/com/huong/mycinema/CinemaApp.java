package com.huong.mycinema;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.huong.mycinema.deps.DaggerDeps;
import com.huong.mycinema.deps.Deps;
import com.huong.mycinema.networking.NetworkModule;

import java.io.File;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class CinemaApp extends AppCompatActivity {
    Deps deps;
    private static CinemaApp mSelf;
    private Gson mGSon;

    public static CinemaApp self() {
        return mSelf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
        mSelf = this;
        mGSon = new Gson();

    }

    public Deps getDeps() {
        return deps;
    }

    public Gson getGSon() {
        return mGSon;
    }
}
