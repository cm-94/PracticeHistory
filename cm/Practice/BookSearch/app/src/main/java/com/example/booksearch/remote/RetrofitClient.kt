package com.example.booksearch.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = "https://openapi.naver.com/v1/search/"
    private lateinit var retrofit: Retrofit

    private var client = OkHttpClient()
    private var builder = OkHttpClient.Builder()


    /**
     * getClient(context)
     * @param context
     * @return retrofit : Retrofit
     *  - 통신을 위한 retrofit 객체를 return
     */
    fun getClient(): Retrofit {
        builder.addInterceptor { chain: Interceptor.Chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", "C5lgKUkRqRLJy79ksMpE")
                .addHeader("X-Naver-Client-Secret", "hA9aUSVBAB")
                .build()
            chain.proceed(request)
        }
        // client : OkHttp 클라이언트 생성
        client = builder.build()
        // retrofit에 기본 URL 등록
        retrofit = Retrofit.Builder().baseUrl(this.BASE_URL)
            // JSON 파싱이 가능하도록 GsonConverterFactory 세팅
            .addConverterFactory(GsonConverterFactory.create())
            // retrofit에 OkHttp Client 세팅
            .client(client)
            // retrofit client build
            .build()
        // 생성된 retrofit client 반환
        return retrofit
    }
}