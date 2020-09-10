package com.example.mybtchat.utils;

import android.content.Context;
import com.example.mybtchat.data.remote.ElderlyService;
import com.example.mybtchat.data.remote.RetrofitCient;


public class RetroUtil {
    public static ElderlyService getElderlyService(Context context) {
        return RetrofitCient.getClient(context).create(ElderlyService.class);
    }
}
