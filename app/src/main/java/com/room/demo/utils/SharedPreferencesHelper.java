package com.room.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public static void putFloat(Context context, String key, float value) {
        getSharedPreferences(context).edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key) {
        return getSharedPreferences(context).getFloat(key, Constants.FLOAT_DEFAULT_VALUE);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES_USER_INFO, Context.MODE_PRIVATE);
    }
}
