package com.example.elderlyversion.data.remote;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = (HashSet<String>) MySharedPreferences.getInstanceOf(context).getHashSet(MySharedPreferences.KEY_COOKIE, new HashSet<String>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
                Log.d("RECIEVE-COOKIE ADDED: ",header);
            }
            MySharedPreferences.getInstanceOf(context).putHashSet(MySharedPreferences.KEY_COOKIE, cookies);
        }

        return originalResponse;
    }
}
