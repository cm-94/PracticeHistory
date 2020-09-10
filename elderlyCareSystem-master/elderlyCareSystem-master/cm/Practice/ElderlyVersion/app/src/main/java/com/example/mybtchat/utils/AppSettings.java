package com.example.mybtchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    // Constants
    public static final int SETTINGS_BACKGROUND_SERVICE = 1;
    public static final int SETTINGS_WEIGHT = 2;


    private static boolean mIsInitialized = false;
    private static Context mContext;

    // Setting values
    private static boolean mUseBackgroundService;
    private static int mWeight;


    public static void initializeAppSettings(Context c) {
        if(mIsInitialized)
            return;

        mContext = c;

        // Load setting values from preference
        mUseBackgroundService = loadBgService();

        mIsInitialized = true;
    }


    /**
     * Load 'Run in background' setting value from preferences
     * @return  boolean    is true
     */
    public static boolean loadBgService() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean isTrue = prefs.getBoolean(Constants.PREFERENCE_KEY_BG_SERVICE, false);
        return isTrue;
    }

    /**
     * Returns 'Run in background' setting
     * @return  boolean    is true
     */
    public static boolean getBgService() {
        return mUseBackgroundService;
    }

    /**
     * Load 'Run in background' setting value from preferences
     * @return    User's weight
     */


    /**
     * Returns 'Run in background' setting
     * @return  int    User's weight
     */
    public static int getWeight() {
        return mWeight;
    }

}
