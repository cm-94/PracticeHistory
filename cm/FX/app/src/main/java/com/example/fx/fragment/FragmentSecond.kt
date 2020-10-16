package com.example.fx.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fx.R
import com.example.fx.data.NewOrder
import com.example.fx.utils.Constants
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.fragment_second.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSecond.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSecond : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("Fragment", "Second Fragment")

        return inflater.inflate(R.layout.fragment_second, container, false)
    }


    override fun onStart() {
        super.onStart()
        /** 발주가능수량  & 주문 수량 TextView*/
        val maxAmount :String =formatter.format(NewOrder.balance)
        max_quantity_value2.text = maxAmount
        NewOrder.callAmount = NewOrder.callUnit*NewOrder.callUnitCount
        call_quantity_value2.text = formatter.format(NewOrder.callAmount)

        /** 주문 수량 SPButton */
        call_button.setDataType(NewOrder.callUnitCount, Constants.CALL_QUANTITY)
//        Log.d("Fragment1","call_button data:"+call_button.dataEditText.text.toString())

        /** 주문수량 Radio Button ( x1,000 x10,000 )*/
        // 초기 NewOrder.callUnit 값 (0 or 1,000 or 10,000) 에 따른 Radio Button 체크 상태 설정
        when(NewOrder.callUnit){
            0->radio_group_call.check(R.id.radio_button_1000)
            1000->radio_group_call.check(R.id.radio_button_1000)
            10000->radio_group_call.check(R.id.radio_button_10000)
        }
        // call Radio Button 클릭 시 NewOrder.callUnit 변경 & 주문수량 TextView 변경
        radio_group_call.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.radio_button_1000-> NewOrder.callUnit = 1000
                R.id.radio_button_10000-> NewOrder.callUnit = 10000
            }
            // 주문 수량 데이터 변경( 수량 x 단위 )
            NewOrder.callAmount = call_button.getData().toInt()*NewOrder.callUnit
            call_quantity_value2.text = formatter.format(NewOrder.callAmount)
        }

        /** Max 버튼 리스너 */
        maxCallButton.setOnClickListener {
            /** 주문 수량 Data & TextView */
            NewOrder.callUnitCount = NewOrder.balance/ NewOrder.callUnit            // 주문수량 계산
            call_button.setDataType(NewOrder.callUnitCount,Constants.CALL_QUANTITY) // call 버튼(EditText) 새로 입력
            call_quantity_value2.text = maxAmount                                          // 주문수량 표기(최상단)
        }

        /** 주문수량 증감(+,-) 버튼 리스너 */
        call_button.minusButton.setOnClickListener { /** (ㅡ) 버튼!! */
            NewOrder.callUnitCount = call_button.calcData(Constants.SUB_NUMBER).toInt()
            NewOrder.callAmount = NewOrder.callUnitCount*NewOrder.callUnit
            call_quantity_value2.text = formatter.format(NewOrder.callAmount)
        }
        call_button.plusButton.setOnClickListener { /** (+) 버튼!! */
            NewOrder.callUnitCount = call_button.calcData(Constants.ADD_NUMBER).toInt() // 주문 개별 수량
            NewOrder.callAmount = NewOrder.callUnitCount*NewOrder.callUnit              // 주문 총 수량 = 주문 개별 수량 X 주문 단위
            call_quantity_value2.text = formatter.format(NewOrder.callAmount)
        }

        /** SP Button 길게 클릭 시 해당 EditText값을 바로 call_quantity_value(주문수량) TextView에 반영!! */
        call_button.inputData.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                NewOrder.callUnitCount = s.toString().toInt() // 주문 개별 수량
                NewOrder.callAmount = NewOrder.callUnitCount*NewOrder.callUnit              // 주문 총 수량 = 주문 개별 수량 X 주문 단위
                call_quantity_value2.text = formatter.format(NewOrder.callAmount)
                Log.d("TextWatcher!!","NewOrder.callAmount:${NewOrder.callAmount},char:$s")
            }
            override fun afterTextChanged(arg0: Editable) {
                // 입력이 끝났을 때
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /** 지정 & 역지정 SPButton 리스너!! */
        // +,- 처음 클릭 시 -> 매수 호가로 세팅
        // 이후 클릭 시 0.001씩 증감
        /** 지정 */
        designation_value.minusButton.setOnClickListener { // (-) 버튼!!
            if(designation_value.getData().isEmpty()){
                designation_value.setDataType(NewOrder.callprice,Constants.LIMIT_PRICE) // 매수 호가로 pips 설정
                order_confirm.isEnabled = true                                          // 주문 확인버튼 비활성화
            }else {
                designation_value.setDataType(
                    designation_value.calcData(Constants.SUB_NUMBER).toFloat()          //-> 뺄셈 계산 실행
                    ,Constants.LIMIT_PRICE)                                             // 새로 데이터 세팅
            }
        }
        designation_value.plusButton.setOnClickListener { // (+) 버튼!!
            if (designation_value.getData().isEmpty()) {
                designation_value.setDataType(NewOrder.callprice, Constants.LIMIT_PRICE) // 매수 호가로 pips 설정
                order_confirm.isEnabled = true                                           // 주문 확인버튼 비활성화
            } else {
                designation_value.setDataType(
                    designation_value.calcData(Constants.ADD_NUMBER).toFloat()           //-> 뺄셈 계산 실행
                    , Constants.LIMIT_PRICE)                                             // 새로 데이터 세팅
            }
        }

        /** 역지정 */
        designation_station_value.minusButton.setOnClickListener { // (-) 버튼!!
            if(designation_station_value.getData().isEmpty()){
                designation_station_value.setDataType(NewOrder.callprice,Constants.LIMIT_PRICE) // 매수 호가로 pips 설정
                order_confirm.isEnabled = true                                                  // 주문 확인버튼 비활성화
            }else {
                designation_station_value.setDataType(
                    designation_station_value.calcData(Constants.SUB_NUMBER).toFloat()          //-> 뺄셈 계산 실행
                    ,Constants.LIMIT_PRICE)                                                     // 새로 데이터 세팅
            }
        }
        designation_station_value.plusButton.setOnClickListener { // (+) 버튼!!
            if (designation_station_value.getData().isEmpty()) {
                designation_station_value.setDataType(NewOrder.callprice, Constants.LIMIT_PRICE) // 매수 호가로 pips 설정
                order_confirm.isEnabled = true                                                   // 주문 확인버튼 비활성화
            } else {
                designation_station_value.setDataType(
                    designation_station_value.calcData(Constants.ADD_NUMBER).toFloat()           //-> 뺄셈 계산 실행
                    , Constants.LIMIT_PRICE)                                                     // 새로 데이터 세팅
            }
        }

        // TODO : 주문수량 == 0 / (역)지정 중 하나라도 0이면 order_confirm 버튼 비활성화 되도록 하기!!

    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSecond.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSecond().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun refresh(){

        val tr = fragmentManager?.beginTransaction()
        tr?.detach(this)?.attach(this)

    }
}