package com.example.bit.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bit.R
import com.example.bit.data.Exchange
import com.example.bit.data.ExchangeData
import com.example.bit.data.TickerMain
import com.example.bit.data.remote.BitService
import com.example.bit.utils.Constants
import com.example.bit.utils.RetrofitUtils
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    // View & Adapter
    // Recycler View 초기화 할 LinearLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var myAdapter : MainAdapter
    // Adapter에서 View를 구성하는데 쓰일 ArrayList
    private var arr : ArrayList<TickerMain> = ArrayList()

    // TickerMain Data
    private var paymentCurrency : String = Constants.PAYMENT_CURRENCY_KRW
    private var currentExchangeRate : Float = 1F  // 현재 적용 환율
    private var exchangeRateKRW : Float = 1F      // 한화( == 1..)
    private var exchangeRateUSD : Float = 0.0F    // 달러
    private var exchangeRateJPY : Float = 0.0F    // 엔화

    // spinner data
    private var mExchangeList : ArrayList<Exchange> = arrayListOf()
    private lateinit var mExchangeAdapter: ExAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myAdapter = MainAdapter(applicationContext,arr)

        // Recycler View 초기화
        // LinearLayout : 세로, 정방향 Scroll
        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = linearLayoutManager
        // adapter
        recyclerView.adapter = myAdapter

        /** tickerName( 종목명(달러/$) ) -> 현재 통화화폐 기본값(=KRW)으로 설정 */
        // tickerName.text = applicationContext.getString(R.string.ticker_name,R.string.KRW)
        //  -> R.string에 R.string을 넣으려고 하면 에러.. => Constants 클래스 사용!!
        tickerName.text = getString(R.string.exchange_current,Constants.KRW)

        buttonSetting() // 버튼(3개) 세팅하기
    }

    override fun onResume() {
        super.onResume()
        // 환율 정보 가져오기
        getExchangeRate()

//            getExchangeRate() // => Throwing OutOfMemoryError : 매번 client를 생성하므로 onCreate()에서만 실행..
//            getTickerData(Constants.ALL_CURRENCY)
        /**
         * - 데이터(All -> 110개) 수신요청
         * - 주기 : 1.2초
         */
        timer(period=1200){
            getTickerData(Constants.ALL_CURRENCY)
        }

    }

    private fun buttonSetting(){
        //TODO : 1. Ticker & Orderbook 데이터 수신
        // Ticker : ticker/{order_currency}_{payment_currency}
        // OrderBook : orderbook/{order_currency}_{payment_currency}
        /** 버튼(3개) 클릭 리스너 등록 **/
//        tickerButton.setOnClickListener {
            // TODO : 통화 화폐 설정값(paymentCurrency)  showAllTickerData의 두번째 인자로 넣어주기!!
//            getTickerData(Constants.ALL_CURRENCY)
//        }

        //TODO : 2.1 환율 spinner(spinner) => payment_currency 변경


        //TODO : 2.2 환율 버튼(paymentButton) => payment_currency 변경
//        paymentButton.setOnClickListener {
//            /** 현재 환율 가져오기 */
////            getExchangeRate()
//            when(paymentCurrency){
//                /** 한화 일 때 */
//                Constants.PAYMENT_CURRENCY_KRW->{
//                    /** 달러로 Text 변경 */
//                    paymentCurrency = Constants.PAYMENT_CURRENCY_USD
//                    tickerName.text = applicationContext.getString(R.string.exchange_current,Constants.USD)
//                    /** 현재 환율(currentExchangeRate) 변경 */
//                    currentExchangeRate = exchangeRateUSD
//                }
//                /** 달러 일 때 */
//                Constants.PAYMENT_CURRENCY_USD->{
//                    /** 엔화로 Text 변경 */
//                    paymentCurrency = Constants.PAYMENT_CURRENCY_JPY
//                    tickerName.text = applicationContext.getString(R.string.exchange_current,Constants.JPY)
//                    /** 현재 환율(currentExchangeRate) 변경 */
//                    currentExchangeRate = exchangeRateJPY
//                }
//                /** 엔화 일 때 */
//                Constants.PAYMENT_CURRENCY_JPY->{
//                    /** 엔화로 Text 변경 */
//                    paymentCurrency = Constants.PAYMENT_CURRENCY_KRW
//                    tickerName.text = applicationContext.getString(R.string.exchange_current,Constants.KRW)
//                    /** 현재 환율(currentExchangeRate) 변경 */
//                    currentExchangeRate = exchangeRateKRW
//                }
//            }
//            //TODO : 3. orderbook 데이터 검색(orderbook/{order_currency}_{payment_currency})
//            searchButton.setOnClickListener {
//            }
//        }
    }

    /**
     * @param
     * @return none
     *  - 환율 spinner 초기화
     */
    fun initList(){
        mExchangeList.add(Exchange(Constants.PAYMENT_CURRENCY_KRW,R.drawable.dollar))
        mExchangeList.add(Exchange(Constants.PAYMENT_CURRENCY_USD,R.drawable.won))
        mExchangeList.add(Exchange(Constants.PAYMENT_CURRENCY_JPY,R.drawable.yen))

        mExchangeAdapter = ExAdapter(this, mExchangeList)

        // TODO 3. Spinner.adpater 초기화
        var spinner : Spinner = findViewById(R.id.spinner)
        spinner.adapter = mExchangeAdapter
    }


    /**
     * showAllData()
     * @param
     * @return none
     *  - 전체(110개) Ticker(현재 시세) 데이터 수신
     *  - myAdapter에 110개 데이터를 담아 recycler view에 표시
     */
    private fun getTickerData(order:String){
        // RetrofitUtils.getBitService(applicationContext) -> 톹신에 사용될 client 생성 및 BitService return
        //  -> BTC 종목에 대한 Ticker 데이터를 받아온다. => @GET, URI : .../ticker/BTC_KRW.
        // enqueue( ... ) -> 해당 URL로 요청을 보낸다
        // object : retrofit2.Callback<ResponseBody> { ... } -> 전달 받은 응답(CallBack)에 대한 처리
        RetrofitUtils.getBitService(applicationContext).getAllTicker(order)?.enqueue(object : retrofit2.Callback<HashMap<String, Any>> {
            // 요청이 성공했을 경우(서버에 요청이 전달 된 상태)
            override fun onResponse(call: Call<HashMap<String, Any>?>?,response: Response<HashMap<String, Any>?>) {
                // 정상 Callback을 받은 경우 ( status == 0000 )
                if (response.body()?.get("status") == "0000") {
                    /** key가 data인 items(value)에 대해 */
                    response.body()?.get("data").let{ items->
                        /** items의 자료형이 LinkedTreeMap인 경우 */
                        if(items is LinkedTreeMap<*,*>|| items is ArrayList<*>){
                            /** items의 각 value에 대해 */
                            items.let {
                                it as LinkedTreeMap<*,*>
                                var ticker : TickerMain
                                arr.clear() // data 꺼내기 전에 arr 비워두기!!
                                it.forEach { (order_currency, data) ->
                                    /** order_currency = 종목명, data = { 시가 = ..., 종가 = ..., 저가 = ... } */

                                    /** 마지막 데이터 == date(날짜)이므로 제외하고 Map으로 캐스팅!! */
                                    if (data is Map<*, *>){
                                        ticker = TickerMain(
                                            // data["key"] => Any 타입 데이터들..
                                            // String으로 캐스팅 해서 TickerData 초기화!!
                                            //  + Number로 캐스팅이 안됨..ㅠㅠ => java.lang.String cannot be cast to java.lang.Number
                                            currentExchangeRate,
                                            paymentCurrency,
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
                                        arr.add(ticker)
                                    }else{
                                        /** timeSet(data) => 화면에 시간 표시하기 */
//                                        Log.d("MainActivity_it","any1: "+order_currency+", data: "+data)
                                        setTime()
                                    }
                                }
                            }
                        }
                    }
                    // TODO : 1.1 응답받은 전체 데이터를 MainAdapter에 담아 화면에 보여주기
                    myAdapter.addItems(arr)
                    // Adapter로 화면 내 데이터 새로고침
                    myAdapter.notifyDataSetChanged()

                }
                // 정상 Callback을 받지 못한 경우( ex. 404 error )
                else {
                    Log.d("MainActivity", "Not Successful or Empty Response")
                }
            }
            // 요청이 실패했을 경우(서버에 요청이 전달되지 못한 상태)
            override fun onFailure(
                call: Call<HashMap<String, Any>?>?, t: Throwable ) {
                Log.d("MainActivity", "Connect_Error "+t.cause)
                t.printStackTrace()
            }
        })
    }

    /**
     * getExchangeRate
     * @param none
     * @return none
     *  1. retrofit client 생성한다. ( 데이터 통신을 위한 retrofit이 이미 object로 사용되므로.. )
     *  2. 한미일 환율 데이터를 가져온다( List<ExchangeRate> , https://api.manana.kr/ ... )
     *  3. 환율 변수 exchangeRateXXX 에 수신 데이터 값으로 초기화(변경) 한다.(Activity의 클래스 변수)
     */
    private fun getExchangeRate(){
        val client: OkHttpClient
        val builder = OkHttpClient.Builder()
        client = builder.build()
        // retrofit에 기본 URL 등록
        val retrofit : Retrofit = Retrofit.Builder().baseUrl("https://api.manana.kr/")
            // JSON 파싱이 가능하도록 GsonConverterFactory 등록
            .addConverterFactory(GsonConverterFactory.create()).
                // retrofit에 OkHttp Client 등록
            client(client)
            // retrofit client build
            .build()

        retrofit.create(BitService::class.java).getExchangeRate()?.enqueue(object : retrofit2.Callback<List<ExchangeData>>{
            override fun onResponse(
                call: Call<List<ExchangeData>>,
                response: Response<List<ExchangeData>>
            ) {
                response.body()?.get(1)?.rate?.let { exchangeRateUSD = it }
                response.body()?.get(2)?.rate?.let { exchangeRateJPY = it }
//                Log.d("MainActivity","Exchange "+ response.body()!![1].rate)
            }

            override fun onFailure(call: Call<List<ExchangeData>>, t: Throwable) {
                Log.d("MainActivity","Exchange err"+t.message)
            }
        })
    }

    /**
     * setTime()
     *  - Calendar 클래스를 이용해 현재시간을 받아와
     *  - tickerTime.text에 반영 ( format : R.string.ticker_date )
     */
    fun setTime(){
        // 현재 시간 받아오기( Calendar )
        val date  = Calendar.getInstance().time
        tickerTime.text = applicationContext.getString(R.string.ticker_date,date.year-100,date.month,date.date,date.hours,date.minutes,date.seconds)
        tickerTime.visibility = View.VISIBLE
    }
}