package com.example.bit

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import kotlinx.android.synthetic.main.ticker_item.view.*
import kotlinx.coroutines.*
import java.text.DecimalFormat

class MyTextView : androidx.appcompat.widget.AppCompatTextView{
    private var prevText :String = "0.0"
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 새로운 값으로 Text를 변경될 때 이전 값과 비교하여 배경색 변경
     * prevText > currentText -> 감소 : 파란색(-1)
     * prevText < currentText -> 감소 : 빨간색(1)
     * prevText = currentText -> 동일 : 흰색(0)
     *
     * TextView에서 thread직접 배경색 변경
     * @param String
     * @return Int
     */
    fun inputText(currentText:String) {
//        when(this.Compare(this.prevText,currentText)){
//            1->{
//                CoroutineScope(Dispatchers.Default).launch {
//                    super.setBackgroundColor(Color.RED)
//                    this.
//                }
//            }
//            -1->{
//                CoroutineScope(Dispatchers.Default).launch {
//                    super.setBackgroundColor(Color.BLUE)
//                }
//            }
//            0->{
//                super.setBackgroundColor(Color.WHITE)
//
//            }
//        }

//        when(this.Compare(this.prevText,currentText)){
//            /** 데이터의 증,감에 따른 background color 변경 */
//            /** coroutine GlobalScope.launch를 통한 비동기 처리 */
//            1-> {
//                GlobalScope.launch(Dispatchers.Default) {
//                    super.setBackgroundColor(Color.BLUE)
//                }
//            }
//            -1->{
//                GlobalScope.launch(Dispatchers.Default) {
//                    super.setBackgroundColor(Color.RED)
//                }
//            }
//            0->{
//                super.setBackgroundColor(Color.WHITE)
//            }
//        }
//        val a = Compare(this.prevText,currentText)
//        this.prevText = currentText
        this.setText(currentText.setData())
//        changBackground(Compare(prevText,currentText))
//        return a


    }

    /**
     * 이전,현재 데이터 비교(Float)
     * @param String, String
     * @return Int
     */
    fun Compare(prevStr:String,curStr:String):Int{
        var prev:Float = prevStr.toFloat()
        var cur:Float = curStr.toFloat()
        Log.d("MyTextView",prevStr + ", " + curStr)
        if(cur<prev){
            return -1
        }else if(cur>prev){
            return 1
        }else
            return 0
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





    fun changBackground(i : Int){
        when(i){
            1-> {
                super.setBackgroundColor(Color.BLUE)
                Thread.sleep(200)
                super.setBackgroundColor(Color.WHITE)
            }
            -1->{
                super.setBackgroundColor(Color.RED)
                Thread.sleep(200)
                super.setBackgroundColor(Color.WHITE)
            }
            0->{

            }
        }

    }


}

