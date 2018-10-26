package com.huong.mycinema.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class SharedPreference {
    public static void saveString(Application app, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(app);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    public static void deleteString(Application app, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(app);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.remove(key);
        prefEditor.apply();
    }

    public static String getString(Application app, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(app);
        return pref.getString(key, null);
    }
}
