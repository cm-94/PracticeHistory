package com.example.bit.data.remote

import com.example.bit.data.TickerData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BitService {

    @GET("ticker/ALL_KRW")
    fun allTicker(): Call<TickerData>?

    @GET("ticker/ALL_KRW")
    fun BTCTicker(): Call<ResponseBody>?

//    @GET("ticker/{order_currency}_KRW")
//    fun BTCTicker(@Path("order_currency") order:String): Call<TickerData>?


}