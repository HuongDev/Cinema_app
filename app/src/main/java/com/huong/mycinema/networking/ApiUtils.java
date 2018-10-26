package com.huong.mycinema.networking;

import com.huong.mycinema.BuildConfig;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class ApiUtils {
    public static NetworkService getService() {
        return ApiClient.getClient(BuildConfig.BASEURL).create(NetworkService.class);
    }
}
