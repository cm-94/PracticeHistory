package com.example.elderlycaresystem.data.remote;

import com.example.elderlycaresystem.data.elderly.ElderlyInfo;
import com.example.elderlycaresystem.data.info.ElderlyData;
import com.example.elderlycaresystem.data.login.LoginData;
import com.example.elderlycaresystem.data.login.LoginData2;
import com.example.elderlycaresystem.data.login.LoginResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ElderlyService {

    /** Test Version **/

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginData loginData);

    @GET("devices")
    Call<List<ElderlyInfo>> getElderlyList(@Query("uid") String id);

    @GET("devices/{num}/curdata")
    Call<ElderlyData> getElderlyData(@Path("num") int key);

    @GET("devices/{num}")
    Call<ElderlyInfo> getElderlyInfo(@Path("num") int key);

    @GET("users/logout")
    Call<ResponseBody> logout();



    /** LoopBack Version **/

    @GET("login")
    Call<LoginResponse> getTestLogin();


    @GET("devices")
    Call<List<ElderlyInfo>> getTestList();

    @GET("elderly")
    Call<ElderlyData> getTestData();


    /** Final Version **/

    @POST("users/login")
    Call<LoginResponse> login2(@Body LoginData2 loginData);

}
