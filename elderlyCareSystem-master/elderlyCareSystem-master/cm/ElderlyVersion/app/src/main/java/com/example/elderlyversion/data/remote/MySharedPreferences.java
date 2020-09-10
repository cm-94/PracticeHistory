package com.example.elderlyversion.data.remote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class MySharedPreferences {
    public static final String KEY_COOKIE = "KEY_COOKIE";
    private static final String PREF_NAME = "ELDERLY_PREF";

    private static MySharedPreferences dsp = null;

    public static MySharedPreferences getInstanceOf(Context c){
        if(dsp==null){
            dsp = new MySharedPreferences(c);
        }
        return dsp;
    }
///////////////////////////////////////////////////////////////////////////////////////////
    private Context mContext;
    private SharedPreferences pref;

    public MySharedPreferences(Context c) {
        mContext = c;
        pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    public void putHashSet(String key, HashSet<String> set){
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, set);
        editor.commit();
    }

    public HashSet<String> getHashSet(String key, HashSet<String> dftValue){
        try {
            return (HashSet<String>)pref.getStringSet(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }
}
