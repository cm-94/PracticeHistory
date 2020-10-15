package com.example.fx.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.fx.R
import com.example.fx.utils.Constants
import kotlinx.android.synthetic.main.button_item.view.*

class SPButton(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private lateinit var minusButton : Button
    private lateinit var plusButton : Button
    private lateinit var dataEditText: EditText
    private var bDataType : Int = 0

    init {
        initView()
    }

    private fun initView() {
        val view = View.inflate(this.context, R.layout.button_item, this)

        this.minusButton = view.minusButton
        this.plusButton = view.plusButton
        this.dataEditText = view.inputData

        plusButton.setOnLongClickListener {

            true
        }

        view.minusButton.setOnClickListener {
            calcData(Constants.SUB_NUMBER)
        }

        view.plusButton.setOnClickListener {
            calcData(Constants.ADD_NUMBER)
        }
    }



    /**
     * SP Button 에 사용될 버튼의 타입(bDataType) 설정
     * 표시할 첫 데이터와 type을 param으로 받아 EditText에 표현
     * @param data, type
     * @return true,false
     */
    fun setDataType(data:Number, type:Int):Boolean{
        /** type == 1~3 아닐 때 */
        if (type>3 || type<1){
            return false
        }
        //TODO : DataType과 EditText에 대한 설정
        bDataType = type
        when(bDataType){
            /** 매수 매도 수량일 때 : 자연수 */
            Constants.CALL_QUANTITY->{
//                dataEditText.setText(firstData.toInt().toString())
                dataEditText.setText(resources.getString(R.string.callData,data.toInt()))
            }
            /** pips 일 때 : 실수(0.1)*/
            Constants.PIPS_QUANTITY->{
                dataEditText.setText(resources.getString(R.string.pipData,data.toFloat()))
            }
            /** (역)지정가 일 때 : 실수(0.001) */
            Constants.LIMIT_PRICE->{
//                dataEditText.setText(String.format("%.3f", firstData))
                dataEditText.setText(resources.getString(R.string.limitData,data.toFloat()))
            }
        }
        return true
    }

    /**
     * - 데이터(String)와 계산 타입(add,sub String)을 매개변수로 받는다
     * - bDataType과 calcType에 따른 연산 실행 후 EditText에 표시할 Data(String)을 반환한다.
     * @param calcData,calcType
     * @return new data(String)
     */
    fun calcData(calcType:String) :String {
        val data = dataEditText.text.toString()
        var result = ""
        when(bDataType){
            /** 매수 매도 수량일 때 */
            Constants.CALL_QUANTITY->{
                /** 덧셈일 때 => 1씩 변경 */
                result =
                    if (calcType== Constants.ADD_NUMBER) {
                        (data.toInt() + 1).toString()
                    }
                    /** 뺄셈일 때 */
                    else{
                    /** 양수일 때 */
                        if (data.toInt()>0){
                            (data.toInt()-1).toString()
                        }else{
                            "0"
                        }
                    }
            }
            /** pips 일 때 => 0.1씩 변경 */
            Constants.PIPS_QUANTITY->{
                /** 덧셈일 때 */
                result = if (calcType== Constants.ADD_NUMBER){
                    // ex) 3.3pips => 3.3 => 3.4 => 3.4pips => return!!
                    resources.getString(R.string.pipData,(data.substring(0,data.length-4).toFloat()+0.1))
                }/** 뺄셈일 때 */
                else{
                    /** 양수일 때 */
                    if (data.substring(0,data.length-4).toFloat()>0.0){
                        resources.getString(R.string.pipData,(data.substring(0,data.length-4).toFloat()-0.1))
                    }else{
                        "0.0pips"
                    }
                }
            }
            /** (역)지정가 일 때 => 0.01씩 변경 */
            Constants.LIMIT_PRICE->{
                /** 덧셈일 때 */
                result = if (calcType== Constants.ADD_NUMBER){
                    (data.toFloat()+0.001).toString()
                }/** 뺄셈일 때 */
                else{
                    /** 양수일 때 */
                    if (data.toFloat()>0.000){
                        (data.toFloat()-0.001).toString()
                    }else{
                        "0.000"
                    }
                }
            }
        }
        /** EditText에 변경된 데이터로 세팅!! */
        dataEditText.setText(result)
        return result
    }

    /**
     * - set dataEditText to Max count
     * @param amount(Int), option(1,000 or 10,000)
     */
    fun setTextData(data:String){
        dataEditText.setText(data)
    }
//    fun setMaxCall(format: NumberFormat):String{
//        val maxAmount :String
//        if(this.bDataType==Constants.CALL_QUANTITY){
//            maxAmount= formatter.format(NewOrder.balance/NewOrder.callAmount)
//            dataEditText.setText(maxAmount)
//            return maxAmount
//
//            when(dataType){
//                Constants.CALL_QUANTITY->dataEditText.setText(data)
//                Constants.PIPS_QUANTITY->dataEditText.setText(resources.getString(R.string.pipData,data.toFloat()))
//                Constants.LIMIT_PRICE->dataEditText.setText(resources.getString(R.string.limitData,data.toFloat()))
//            }
//        }else{
//            return ""
//        }
//    }

    fun getData():String{
        return dataEditText.text.toString()
    }
}