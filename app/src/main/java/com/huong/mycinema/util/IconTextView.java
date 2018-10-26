package com.huong.mycinema.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by HuongPN on 10/18/2018.
 */
public class IconTextView extends android.support.v7.widget.AppCompatTextView {

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setType(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setType(context);
    }

    public IconTextView(Context context) {
        super(context);
        init();
        setType(context);
    }

    private void setType(Context context) {
        Typeface typeFaceFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(this, typeFaceFont);
    }

    private void init(){
        if (!isInEditMode()){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome.ttf");
            setTypeface(tf);
        }
    }
}
