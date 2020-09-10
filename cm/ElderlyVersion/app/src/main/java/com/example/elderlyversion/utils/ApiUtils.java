package com.example.elderlyversion.utils;

import android.content.Context;

import com.example.elderlyversion.data.remote.ElderlyService;
import com.example.elderlyversion.data.remote.RetrofitCient;

public class ApiUtils {
    public static ElderlyService getElderlyService(Context context) {
        return RetrofitCient.getClient(context).create(ElderlyService.class);
    }
}
