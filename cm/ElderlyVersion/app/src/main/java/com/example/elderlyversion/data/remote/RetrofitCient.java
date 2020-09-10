package com.example.elderlyversion.data.remote;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCient {
    private static Retrofit retrofit = null;
//    private static final String BASE_URL = "http://192.168.43.108:8080/";
//    private static final String BASE_URL = "http://192.168.1.221:8080/";
    private static final String BASE_URL = "http://192.168.1.29:9090/elderlycare/";

    private static OkHttpClient client = new OkHttpClient();
    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();



    public static Retrofit getClient(Context context)
    {
        builder.addNetworkInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        client = builder.build();

        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
//                    client(client).
                    build();
        }
        return retrofit;
    }
}
