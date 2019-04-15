package com.shif.peterson.tizik.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextView_Ubuntu_Bold extends TextView {

    public MyTextView_Ubuntu_Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView_Ubuntu_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView_Ubuntu_Bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu.bold.ttf");
            setTypeface(tf);
        }
    }

}