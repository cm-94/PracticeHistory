package com.example.bithumb.Fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bit.utils.Constants
import com.example.bit.utils.RetrofitUtils
import com.example.bithumb.R
import com.example.bithumb.TickerAdapter
import com.example.bithumb.data.TickerData
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.fragment_ticker.*
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PAYMENT_CURRENCY = "param1"
private const val EXCHANGE_RATE = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentTicker : Fragment() {
    // View & Adapter
    // Recycler View 초기화 할 LinearLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var myAdapter : TickerAdapter
    // Adapter에서 View를 구성하는데 쓰일 ArrayList
    private var arrTicker : ArrayList<TickerData> = ArrayList()

    // TODO: Rename and change types of parameters
    // TickerMain Data
    var paymentCurrency : String? = null// 현재 적용 환율
    private var currentExchangeRate : Float? = null// 현재 적용 환율

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paymentCurrency = it.getString(PAYMENT_CURRENCY) ?: Constants.PAYMENT_CURRENCY_KRW
            currentExchangeRate = it.getString(EXCHANGE_RATE)?.toFloat() ?: 1F
            Log.d("Fragment1_onCreate","currency: "+paymentCurrency+", rate: "+currentExchangeRate)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Context, RecyclerView
        var view = inflater.inflate(R.layout.fragment_ticker, container, false)
        myAdapter = TickerAdapter(view.context,arrTicker)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        tickerRecyclerView.setLayoutManager(linearLayoutManager)
        // adapter
        tickerRecyclerView.adapter = myAdapter


    }

    //TODO : ticker 데이터 수신
    override fun onResume() {
        super.onResume()
        val handler = Handler()
        val handlerTask = object : Runnable {
            override fun run() {
                // do task
                updateDisplay()
                    handler.postDelayed(this, 500)
            }
        }
        /** timer */
//        timer(period=Constants.TICKER_TIMER){
//            getTickerData(Constants.ALL_CURRENCY)
//        }
        /** handler */
        handler.post(handlerTask)
    }

    private fun updateDisplay() {
        val bundle = arguments
        paymentCurrency = bundle?.getString(PAYMENT_CURRENCY)?:Constants.PAYMENT_CURRENCY_KRW
        currentExchangeRate = bundle?.getString(EXCHANGE_RATE)?.toFloat()?:1F
        Log.d("Fragment1_updateDisplay","param1: "+paymentCurrency+", param2: "+currentExchangeRate)

        getTickerData(Constants.ALL_CURRENCY)
        // TODO : 1.1 응답받은 전체 데이터를 MainAdapter에 담아 화면에 보여주기
        myAdapter.addItems(arrTicker)
        // Adapter로 화면 내 데이터 새로고침
        myAdapter.notifyDataSetChanged()
    }

    private fun getTickerData(order:String){
        // RetrofitUtils.getBitService(applicationContext) -> 톹신에 사용될 client 생성 및 BitService return
        //  -> BTC 종목에 대한 Ticker 데이터를 받아온다. => @GET, URI : .../ticker/BTC_KRW.
        // enqueue( ... ) -> 해당 URL로 요청을 보낸다
        // object : retrofit2.Callback<ResponseBody> { ... } -> 전달 받은 응답(CallBack)에 대한 처리
        RetrofitUtils.getBitService(context).getAllTicker(order)?.enqueue(object : retrofit2.Callback<HashMap<String, Any>> {
            // 요청이 성공했을 경우(서버에 요청이 전달 된 상태)
            override fun onResponse(call: Call<HashMap<String, Any>?>?, response: Response<HashMap<String, Any>?>) {
                // 정상 Callback을 받은 경우 ( status == 0000 )
                if (response.body()?.get("status") == "0000") {
                    /** key가 data인 items(value)에 대해 */
                    response.body()?.get("data").let{ items->
                        /** items의 자료형이 LinkedTreeMap인 경우 */
                        if(items is LinkedTreeMap<*, *> || items is ArrayList<*>){
                            /** items의 각 value에 대해 */
                            items.let {
                                it as LinkedTreeMap<*, *>
                                var ticker : TickerData
                                arrTicker.clear() // data 꺼내기 전에 arr 비워두기!!
                                it.forEach { (order_currency, data) ->
                                    /** order_currency = 종목명, data = { 시가 = ..., 종가 = ..., 저가 = ... } */

                                    /** 마지막 데이터 == date(날짜)이므로 제외하고 Map으로 캐스팅!! */
                                    if (data is Map<*, *>){
                                        ticker = TickerData(
                                            // data["key"] => Any 타입 데이터들..
                                            // String으로 캐스팅 해서 TickerData 초기화!!
                                            //  + Number로 캐스팅이 안됨..ㅠㅠ => java.lang.String cannot be cast to java.lang.Number
                                            currentExchangeRate?:1F,
                                            paymentCurrency?:Constants.PAYMENT_CURRENCY_KRW,
                                            order_currency.toString(),
                                            data["opening_price"].toString(),
                                            data["closing_price"].toString(),
                                            data["min_price"].toString(),
                                            data["max_price"].toString(),
                                            data["units_traded_24H"].toString(),
                                            data["acc_trade_value_24H"].toString(),
                                            data["fluctate_24H"].toString(),
                                            data["fluctate_rate_24H"].toString()// data[R.string.max_price].toString() 안됨..ㅠ
                                        )
                                        // arr에 Ticker 데이터 추가!!
                                        arrTicker.add(ticker)
                                    }else{
                                        /** timeSet(data) => 화면에 시간 표시하기 */
                                        Log.d("FragmentTicker","rate: "+currentExchangeRate+", payment: "+paymentCurrency)
                                    }
                                }
                            }
                        }
                    }
                }
                // 정상 Callback을 받지 못한 경우( ex. 404 error )
                else {
                    Log.d("MainActivity", "Not Successful or Empty Response")
                }
            }
            // 요청이 실패했을 경우(서버에 요청이 전달되지 못한 상태)
            override fun onFailure(
                call: Call<HashMap<String, Any>?>?, t: Throwable ) {
                Log.d("MainActivity", "Connect_Error "+t.message)
                t.printStackTrace()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTicker().apply {
                arguments = Bundle().apply {
                    putString(PAYMENT_CURRENCY, param1)
                    putString(EXCHANGE_RATE, param2)
                    Log.d("Fragment1_newInstance","param1: "+param1+", param2: "+param2)
                    paymentCurrency = param1
                    currentExchangeRate = param2.toFloat()
                }
            }
    }


}

