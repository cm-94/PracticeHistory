package com.example.bit.ui.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bit.R
import com.example.bit.data.RootTickerData
import com.example.bit.data.TickerInfo
import com.example.bit.utils.Constants
import com.example.bit.utils.RetrofitUtils
import kotlinx.android.synthetic.main.activity_info.*
import retrofit2.Call
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    // payment_currency : 해당 종목에 대한 기준 화폐를 나타내는 변수
    // KRW(한화,원)으로 초기화
    private var payment_currency : String = "KRW"
    private var order_currency : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        order_currency = intent.extras?.get(Constants.ORDER_CURRENCY).toString()

        /** order_currency가 null이 아니면 뷰 초기화 진행!! */
        order_currency?.let {  initView() }
        //showOrderData(order_currency,payment_currency)


    }

    // TODO : activity_info 구성 후 View 초기화 하는 함수 만들기
    private fun initView(){
        tickerOrderCurrency.text = order_currency
        showOrderData(order_currency,"KRW")


    }



    // TODO : 파라미터(order,payment)에 해당하는 Ticker 정보 가져오기
    private fun showOrderData(order:String,payment:String){
        RetrofitUtils.getBitService(applicationContext).oneTicker(order,payment)?.enqueue(object:retrofit2.Callback<RootTickerData>{
            override fun onResponse(call: Call<RootTickerData>, response: Response<RootTickerData>) {
                response.body()?.data?.let {
                    tickerOpen.text = it.opening_price
                    tickerClose.text = it.closing_price
                    tickerMin.text = it.min_price
                    tickerMax.text = it.max_price
                }
            }

            override fun onFailure(call: Call<RootTickerData>, t: Throwable) {
                Log.d("InfoActivity",t.message.toString())
            }
        })

    }
}