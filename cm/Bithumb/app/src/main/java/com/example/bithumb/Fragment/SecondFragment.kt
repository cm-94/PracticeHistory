package com.example.bithumb.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bit.utils.RetrofitUtils
import com.example.bithumb.R
import com.example.bithumb.data.order.Order
import com.example.bithumb.data.order.OrderResponse
import kotlinx.android.synthetic.main.fragment_second.*
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var bidArray : ArrayList<Order> = arrayListOf()
    private var askArray : ArrayList<Order> = arrayListOf()


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
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getOrder("BTC")

    }


    private fun getOrder(order:String){
        RetrofitUtils.getBitService(context).getOrder(order)?.enqueue(object : retrofit2.Callback<OrderResponse> {
            // 요청이 성공했을 경우(서버에 요청이 전달 된 상태)
            override fun onResponse(call: Call<OrderResponse>?, response: Response<OrderResponse>) {
                // 정상 Callback을 받은 경우 ( status == 0000 )
                if (response.body()?.status=="0000") {
                    response.body()?.data?.let{
                        order_currency.text = it.order_currency
                        it.bids.let { bidArray = it }
                        it.asks.let { askArray = it }
                        setData()
                    }
                }
                // 정상 response 받지 못한 경우( ex. 404 error )
                else {
                    Log.d("Second_Fragment", "Not Successful or Empty Response")
                }
            }
            // 요청이 실패했을 경우(서버에 요청이 전달되지 못한 상태)
            override fun onFailure(
                call: Call<OrderResponse>?, t: Throwable ) {
                Log.d("MainActivity", "Connect_Error "+t.cause)
                t.printStackTrace()
            }
        })
    }


    fun setData (){
        if(bidArray!=null&&askArray!=null){
            bid1.text = bidArray[0].quantity
            bid2.text = bidArray[1].quantity
            bid3.text = bidArray[2].quantity
            bid4.text = bidArray[3].quantity
            bid5.text = bidArray[4].quantity
            bid6.text = bidArray[5].quantity
            bid7.text = bidArray[6].quantity
            bid8.text = bidArray[7].quantity
            bid9.text = bidArray[8].quantity
            bid10.text = bidArray[9].quantity

            price1.text = bidArray[0].price
            price2.text = bidArray[1].price
            price3.text = bidArray[2].price
            price4.text = bidArray[3].price
            price5.text = bidArray[4].price
            price6.text = bidArray[5].price
            price7.text = bidArray[6].price
            price8.text = bidArray[7].price
            price9.text = bidArray[8].price
            price10.text = bidArray[9].price

            ask1.text = askArray[0].quantity
            ask2.text = askArray[1].quantity
            ask3.text = askArray[2].quantity
            ask4.text = askArray[3].quantity
            ask5.text = askArray[4].quantity
            ask6.text = askArray[5].quantity
            ask7.text = askArray[6].quantity
            ask8.text = askArray[7].quantity
            ask9.text = askArray[8].quantity
            ask10.text = askArray[9].quantity

            price11.text = askArray[0].price
            price12.text = askArray[1].price
            price13.text = askArray[2].price
            price14.text = askArray[3].price
            price15.text = askArray[4].price
            price16.text = askArray[5].price
            price17.text = askArray[6].price
            price18.text = askArray[7].price
            price19.text = askArray[8].price
            price20.text = askArray[9].price
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}