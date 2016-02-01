package com.dsquare.hibour.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Deepthi on 1/21/2016.
 */
public class ProximaSemiBold extends TextView
{

    public ProximaSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ProximaSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaSemiBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf1 = Typeface.createFromAsset(getContext().getAssets(), "fonts/pn_semibold.otf");
        setTypeface(tf1);
    }
}
