package com.example.bit.data.remote

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** - Singleton 패턴 적용을 위해 object로 정의 **/
object RetrofitClient {
        private lateinit var retrofit: Retrofit
        private val BASE_URL = "https://api.bithumb.com/public/"

        private var client = OkHttpClient()
        private var builder = OkHttpClient.Builder()

        /**
         * getClient(context)
         * @param context
         * @return retrofit : Retrofit
         *  - 통신을 위한 retrofit 객체를 return
         *  - context를 param으로 받는 것은 추후 builder의 interceptor를 활용하려고..!
         *     builder.addNetworkInterceptor(new AddCookiesInterceptor(context)); => java..
         *     builder.addInterceptor(new ReceivedCookiesInterceptor(context)); => java..
         */
        fun getClient(context: Context?): Retrofit {

            // client : OkHttp 클라이언트 생성
            client = builder.build()
            // retrofit에 기본 URL 등록
            retrofit = Retrofit.Builder().baseUrl(this.BASE_URL)
                // JSON 파싱이 가능하도록 GsonConverterFactory 등록
                .addConverterFactory(GsonConverterFactory.create()).
                // retrofit에 OkHttp Client 등록
                client(client)
                // retrofit client build
                .build()
            // 생성된 retrofit client 반환
            return retrofit
        }

}