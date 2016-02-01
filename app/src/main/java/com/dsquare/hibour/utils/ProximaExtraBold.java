package com.dsquare.hibour.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Deepthi on 1/21/2016.
 */
public class ProximaExtraBold extends TextView
{

    public ProximaExtraBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ProximaExtraBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaExtraBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf1 = Typeface.createFromAsset(getContext().getAssets(), "fonts/pn_extrabold.otf");
        setTypeface(tf1);
    }
}
