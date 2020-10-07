package com.example.bithumb.data.remote

import com.example.bithumb.data.exchange.ExchangeData
import com.example.bithumb.data.order.OrderResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BitService {
    @GET("ticker/{order_currency}_KRW")
    fun getAllTicker(@Path("order_currency") order:String): Call<HashMap<String, Any>>?

    @GET("orderbook/{order_currency}_KRW")
    fun getOrder(@Path("order_currency") order:String,@Query("count") cnt :Int): Call<OrderResponse>?


    // 환율 데이터 수신 URL
    @GET("exchange/rate/KRW/{exchange}.json")
    fun getExchangeRate(@Path("exchange") exchangeRate:String): Call<List<ExchangeData>>?
}