package com.example.fx

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.button_item.view.*

class SPButton(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private lateinit var minusButton : ImageButton
    private lateinit var plusButton : ImageButton
    private lateinit var dataEditText: EditText
    private var bDataType : Boolean = false
//    private var bDataType : Int = 0

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        val view = View.inflate(this.context, R.layout.button_item, this)
        view.inputData.setText("1")
        view.inputData.setBackgroundColor(Color.WHITE)
        this.minusButton = view.minusButton
        this.plusButton = view.plusButton
        this.dataEditText = view.inputData

        view.minusButton.setOnClickListener {
            when(bDataType){
                Constants.CALL_QUANTITY->{

                }
                Constants.PIP_QUANTITY->{

                }
                Constants.LIMIT_PRICE->{

                }
                Constants.STOP_PRICE->{

                }


            }
            if(isStringDouble(dataEditText.text.toString())){
                var data = dataEditText.text.toString().toInt()
                /** 0 이상 => 데이터 증가 */
                if (data>0){
                    data-=1
                    dataEditText.setText(data.toString())
                    Log.d("SPButton","data:${data}")
                }
                /** 0 이하 => return */
                else{
                    Log.d("SPButton","min data:${data}")
                    return@setOnClickListener
                }
            }
            /** pip data인 경우 */
            else if(bDataType){
                val data = dataEditText.text.toString()
                Log.d("SPButton","pip data:${data}")
            }
            /** 잘못된 입력인 경우 */
            else{
                return@setOnClickListener
            }
        }

        view.plusButton.setOnClickListener {
            if(isStringDouble(dataEditText.text.toString())){
                var data = dataEditText.text.toString().toInt()
                data+=1
                dataEditText.setText(data.toString())
                Log.d("SPButton","data:${data}")
            }
            /** pip data인 경우 */
            else if(bDataType){
                val data = dataEditText.text.toString()
                Log.d("SPButton","pip data:${data}")
            }
            /** 잘못된 입력인 경우 */
            else{
                return@setOnClickListener
            }

        }
    }

    /**
     * 문자열이 Float으로 변환 가능하면 return true
     */
    fun isStringDouble(str: String): Boolean {
        return try {
            str.toFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

}