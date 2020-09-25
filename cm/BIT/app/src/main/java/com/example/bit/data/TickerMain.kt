package com.example.bit.data

import android.service.autofill.FillEventHistory
import android.util.Log
import com.example.bit.utils.Constants
import retrofit2.Converter
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 *  data class 로 정의하면
 * 주 생성자( TickerData( ... ) ) 를 통한 property 초기화가 필요하다.
 * MainActivity에서는 상위 5개 property만,
 * InfoActivity에서는 모든 property를 사용할 것이므로
 * 주 생성자에서 모든 property를 데이터로 초기화 할 필요가 없으므로
 * class로 만듦
 */
class TickerMain (){
    var exchange_rate:Float = 0F
    var payment_currency:String = ""
    var order_currency:String = ""
    var opening_price:String = ""
    var closing_price:String = ""
    var min_price:String = ""
    var max_price:String = ""
    var fluctate_24H:String = ""
    var fluctate_rate_24H:String = ""

    val dataFormat = DecimalFormat("#,###.#")

    // 보조 생성자 : 입력받은 값을 각 맴버변수에 초기화 ( 가격 데이터 -> 천 단위 표시)
    constructor(exchange_rate:Float, payment_currency:String,order_currency:String,
                opening_price:String,closing_price:String,min_price:String,max_price:String,
                fluctate_24H:String,fluctate_rate_24H:String) : this() {
        this.exchange_rate = exchange_rate
        this.payment_currency = payment_currency
        this.order_currency = order_currency
        this.opening_price = opening_price.setData(exchange_rate)
        this.closing_price = closing_price.setData(exchange_rate)
        this.min_price = min_price.setData(exchange_rate)
        this.max_price = max_price.setData(exchange_rate)
        this.fluctate_24H = fluctate_24H.setData(exchange_rate)
        this.fluctate_rate_24H = fluctate_rate_24H
    }

    /**
     * String.setData()
     * @param exchange_rate
     * @return Stirng
     * 클래스의 String 맴버(property)에 대해 화페 format으로 변경된 값을 반환한다
     */
    private fun String.setData(exchange_rate:Float) : String{
        return dataFormat.format(this.toFloat()/exchange_rate).toString()
    }
}

// String Formatting 연습1 //
//    var sb  = StringBuilder()
//    var formatter = Formatter(sb,Locale.US)

//  1) return formatter.format("%(,.2f",this.toFloat()).toString()
//  2) it.opening_price = formatter.format("%(,.s",it.opening_price ).toString()
//     sb.delete(0,sb.length) // => 지우지 않으면 계속 뒤에 붙임...