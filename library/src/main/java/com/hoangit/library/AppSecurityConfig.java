package com.hoangit.library;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSecurityConfig {

    public static final int NO = 0;
    public static final int PASS_CODE = 1;
    public static final int PATTERN = 2;

    public static int getTypeSecurity(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getInt("type_security",0);
    }

    public static void putTypeSecurity(Context context,int i){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("type_security",i).apply();
    }

    public static String getPin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString("pin","0000");
    }
    public static void putPin(Context context,String pin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("pin",pin).apply();
    }

    public static String getPattern(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString("pattern","1234");
    }
    public static void putPattern(Context context,String pin){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppSecurityConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("pattern",pin).apply();
    }

}
