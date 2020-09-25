package com.example.bit.data.remote

import com.example.bit.data.ExchangeRate
import com.example.bit.data.RootTickerData
import com.example.bit.data.TickerInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BitService {
    @GET("ticker/{order_currency}_KRW")
    fun showTicker(@Path("order_currency") order:String): Call<HashMap<String, Any>>?

    @GET("ticker/{order_currency}_{payment_currency}")
    fun oneTicker(@Path("order_currency") order:String,@Path("payment_currency") payment:String): Call<RootTickerData>?






    //
    @GET("exchange/rate/KRW/KRW,USD,JPY.json")
    fun getExchangeRate(): Call<List<ExchangeRate>>?

}