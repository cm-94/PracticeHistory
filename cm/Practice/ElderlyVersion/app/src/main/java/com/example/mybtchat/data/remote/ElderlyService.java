package com.example.mybtchat.data.remote;

import com.example.mybtchat.data.ElderlyData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ElderlyService {

//    @PUT("/users/{key}/datas")
//    Call<ResponseBody> putElderlyData(@Path("key") String id, @Body ElderlyData eldelyData);

//    @POST("http://192.168.1.221:3001/memo")
//    Call<ResponseBody> putElderlyData(@Body ElderlyData eldelyData);

    @GET("devices/{num}/data")
    Call<ResponseBody> getElderlyData(@Path("num") int key);

    @POST("datas")
    Call<ResponseBody> putElderlyData(@Body ElderlyData eldelyData);

    @POST("https://fcm.googleapis.com/fcm/send")
    Call<ResponseBody> emergencyState(@Body ElderlyData eldelyData);
}
