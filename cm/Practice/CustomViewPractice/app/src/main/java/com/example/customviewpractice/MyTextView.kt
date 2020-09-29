package com.example.customviewpractice

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyTextView : androidx.appcompat.widget.AppCompatTextView{
    private var prevText :String = "0.0"
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 새로운 값으로 Text를 변경될 때 이전 값과 비교하여 배경색 변경
     * prevText > currentText -> 감소 : 파란색
     * prevText < currentText -> 감소 : 빨간색
     * prevText = currentText -> 동일 : 흰색(변화 x)
     * 배경색 변경 : GlobalScope -> 비동기 처리
     * @param String
     * @return Unit
     */
    fun inputText(currentText:String) {
        // TODO : currentText -> toFloat 확인
        try {
            when(this.Compare(this.prevText,currentText)){
                /** 데이터의 증,감에 따른 background color 변경 */
                /** coroutine GlobalScope.launch를 통한 비동기 처리 */
                1-> {
                    GlobalScope.launch {
                        super.setBackgroundColor(Color.BLUE)
                        delay(1000) // -> View에서 지양해야 될 행동..
                        super.setBackgroundColor(Color.WHITE)
                    }
                }
                -1->{
                    GlobalScope.launch {
                        super.setBackgroundColor(Color.RED)
                        delay(1000)
                        super.setBackgroundColor(Color.WHITE)
                    }
                }
                0->{

                }
            }
            this.prevText = currentText
            this.setText(currentText)
        }
        catch (e:Exception){
            Log.d("MyTextView", e.message.toString())
        }
    }

    /**
     * 이전,현재 데이터 비교(Float)
     * @param String, String
     * @return Int
     */
    fun Compare(prevStr:String,curStr:String):Int{
        var prev:Float = prevStr.toFloat()
        var cur:Float = curStr.toFloat()
        if(cur<prev){
            return 1
        }else if(cur>prev){
            return -1
        }else
            return 0
    }
}

