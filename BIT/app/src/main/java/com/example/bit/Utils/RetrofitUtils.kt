package com.example.bit.Utils

import android.content.Context
import com.example.bit.data.remote.BitService
import com.example.bit.data.remote.RetrofitClient

object RetrofitUtils {
    fun getBitService(context: Context?): BitService {
        /** Retrofit Client를 생성한 후 BitService를 적용한 후 해당 Service를 return **/
        return RetrofitClient.getClient(context)!!.create(BitService::class.java)
    }

}
