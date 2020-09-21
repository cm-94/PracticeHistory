package com.example.bit.data.remote

import com.example.bit.data.TickerData
import retrofit2.Call
import retrofit2.http.GET

interface BitService {
    @GET("ticker/ALL_KRW")
    fun allTicker(): Call<TickerData>?

    @GET("ticker/ALL_KRW")
    fun BTCTicker(): Call<HashMap<String, Any>>?

//    @GET("ticker/{order_currency}_KRW")
//    fun BTCTicker(@Path("order_currency") order:String): Call<TickerData>?
}