package com.example.booksearch.util

import com.example.booksearch.remote.RetrofitClient
import com.example.booksearch.remote.RetrofitService

object RetrofitUtils {
    /**
     * @return RetrofitClient  RetrofitService로 Retrofit Client를 생성한 후 return
     */
    fun getBookService(): RetrofitService {
        return RetrofitClient.getClient().create(RetrofitService::class.java)
    }
}