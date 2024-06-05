package com.example.myandroidproject.utils;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static final String STATE_LOGIN = "IS_LOGIN";
    public static void add(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String get(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
    public static boolean checkLogin(Context context) {
        return get(STATE_LOGIN, context).equals("TRUE");
    }
}
