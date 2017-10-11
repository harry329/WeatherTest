package com.example.harry.weathertest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.harry.weathertest.BuildConfig;

/**
 * Created by harry on 10/10/17.
 */

public class PreferenceStorage {

    public static void storeValues(Context context, Bundle bundle) {
        SharedPreferences prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(AppConstant.DATA_PRESENT,true);
        editor.putString(AppConstant.ICON,bundle.getString(AppConstant.ICON));
        editor.putString(AppConstant.DESCRIPTION,bundle.getString(AppConstant.DESCRIPTION));
        editor.putString(AppConstant.TEMP_MAX,bundle.getString(AppConstant.TEMP_MAX));
        editor.putString(AppConstant.HUMIDITY,bundle.getString(AppConstant.HUMIDITY));
        editor.putString(AppConstant.SPEED,bundle.getString(AppConstant.SPEED));
        editor.putString(AppConstant.SUNRISE,bundle.getString(AppConstant.SUNRISE));
        editor.putString(AppConstant.CITY,bundle.getString(AppConstant.CITY));
        editor.commit();
    }

    public static Bundle getValues(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.CITY,prefs.getString(AppConstant.CITY,AppConstant.NA));
        bundle.putString(AppConstant.SUNRISE,prefs.getString(AppConstant.SUNRISE,AppConstant.NA));
        bundle.putString(AppConstant.SPEED,prefs.getString(AppConstant.SPEED,AppConstant.NA));
        bundle.putString(AppConstant.HUMIDITY,prefs.getString(AppConstant.HUMIDITY,AppConstant.NA));
        bundle.putString(AppConstant.TEMP_MAX,prefs.getString(AppConstant.TEMP_MAX,AppConstant.NA));
        bundle.putString(AppConstant.DESCRIPTION,prefs.getString(AppConstant.DESCRIPTION,AppConstant.NA));
        bundle.putString(AppConstant.ICON,prefs.getString(AppConstant.ICON,AppConstant.NA));
        bundle.putBoolean(AppConstant.DATA_PRESENT,prefs.getBoolean(AppConstant.DATA_PRESENT,false));
        return bundle;
    }


}
