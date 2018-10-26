package com.huong.mycinema.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = "fontawesome.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void markAsIcon(View[] views, Typeface typeface) {
        for (View view :  views) {
            if (view instanceof TextView)
                ((TextView)view).setTypeface(typeface);
        }
    }

    public static void markAsIconContainer(View v, Typeface typeface) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child, typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        }
    }

}
