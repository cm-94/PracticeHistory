package com.example.bithumb.utils

import android.content.Context
import android.util.AttributeSet
import java.text.DecimalFormat

class MyTextView : androidx.appcompat.widget.AppCompatTextView{
    /** Default constructor */
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * @param String
     * @return Unit
     *  - 화폐단위(#,###.#)로 변환 후 setText
     */
    fun inputText(text:String) {
        this.setText(text.setData())
    }

    /**
     * String.setData()
     * @param exchange_rate
     * @return Stirng
     * 클래스의 String 맴버(property)에 대해 화페 format으로 변경된 값을 반환한다
     */
    val dataFormat = DecimalFormat("#,###.#")

    private fun String.setData() : String{
        return dataFormat.format(this.toFloat()).toString()
    }
}

