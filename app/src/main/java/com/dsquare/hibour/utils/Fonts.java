package com.dsquare.hibour.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by deepthi on 1/21/16.
 */
public class Fonts {
    public static String regularFont = "fonts/pn_regular.otf";
    public static String extraBoldFont = "fonts/pn_extrabold.otf";
    public static String lightFont = "fonts/pn_light.otf";

    public static Typeface getTypeFace(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), regularFont);
    }

    public static Typeface getTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), regularFont);
    }

    public static String getTypeFaceName() {
        return regularFont;
    }

    public static String getExtraBoldFont() {
        return extraBoldFont;
    }

    public static String getLightFont() {
        return lightFont;
    }
}
