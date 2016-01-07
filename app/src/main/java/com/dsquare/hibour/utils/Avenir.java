package com.dsquare.hibour.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ASHOK on 1/7/2016.
 */
public class Avenir extends TextView
{

    public Avenir(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Avenir(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Avenir(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf1 = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Book.otf");
        setTypeface(tf1);
    }
}
