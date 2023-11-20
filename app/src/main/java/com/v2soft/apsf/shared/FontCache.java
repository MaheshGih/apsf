package com.v2soft.apsf.shared;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by srikanth.m on 10/3/2017.
 */

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<String, Typeface>();

    public static Context mContext;

    FontCache(Context context) {
        this.mContext = context;
    }

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }

    public static Typeface getFace(String fontname) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(mContext.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }

    public static Typeface getRegularFont() {
        Typeface typeface = fontCache.get("ui-text-regular.ttf");

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(mContext.getAssets(), "ui-text-regular.ttf");
            } catch (Exception e) {
                return null;
            }

            fontCache.put("ui-text-regular.ttf", typeface);
        }

        return typeface;
    }

    public static Typeface getBoldFont() {
        Typeface typeface = fontCache.get("ui-text-bold.ttf");

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(mContext.getAssets(), "ui-text-bold.ttf");
            } catch (Exception e) {
                return null;
            }

            fontCache.put("ui-text-bold.ttf", typeface);
        }

        return typeface;
    }
}