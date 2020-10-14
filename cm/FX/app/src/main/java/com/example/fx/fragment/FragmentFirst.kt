package com.example.fx.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fx.Constants
import com.example.fx.NewOrder
import com.example.fx.R
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.fragment_first.*
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var formatter: NumberFormat = DecimalFormat("#,###")

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFirst.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFirst : Fragment() {
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
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        Log.d("Fragment", "First Fragment")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Fragment1","onViewCreated 호출됨")
        call_button.setDataType(1, Constants.CALL_QUANTITY)
        Log.d("Fragment1","call_button data:"+call_button.dataEditText.text.toString())
//        pips_button.setDataType(3.0, Constants.PIPS_QUANTITY)
//        Log.d("Fragment1","pips_button data:"+pips_button.dataEditText.text.toString())


        /** 주문 수량 */
        val maxAmount :String =formatter.format(NewOrder.balance)
        max_quantity.text = maxAmount
        call_quantity.text = formatter.format(NewOrder.callAmount)

        maxCallButton.setOnClickListener {

            call_button.setMaxCall((NewOrder.balance/NewOrder.callAmount).toString())

            call_quantity.text = maxAmount
        }
        call_button.minusButton.setOnClickListener {
//            Log.d("Fragment1","minus button"+resources.getString(R.string.call_quantity,call_button.calcData(Constants.SUB_NUMBER)))
            call_quantity.text = formatter.format((call_button.calcData(Constants.SUB_NUMBER).toInt()*NewOrder.callAmount))


        }
        call_button.plusButton.setOnClickListener {
//            Log.d("Fragment1","plus button"+resources.getString(R.string.call_quantity,call_button.calcData(Constants.ADD_NUMBER)))
            call_quantity.text = formatter.format((call_button.calcData(Constants.ADD_NUMBER).toInt()*NewOrder.callAmount))

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentFirst.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentFirst().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}