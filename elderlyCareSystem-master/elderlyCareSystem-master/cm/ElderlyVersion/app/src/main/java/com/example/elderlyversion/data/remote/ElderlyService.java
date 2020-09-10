package com.example.elderlyversion.data.remote;

import com.example.elderlyversion.data.ElderlyData;
import com.example.elderlyversion.data.KeyData;
import com.example.elderlyversion.data.LoginData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ElderlyService {
    @POST("devices/login")
    Call<KeyData> login(@Body LoginData loginData);

    @POST("datas")
    Call<ResponseBody> putElderlyData(@Body ElderlyData eldelyData);

}
