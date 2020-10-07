package com.example.bithumb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.bit.utils.Constants
import com.example.bithumb.Fragment.*
import com.example.bithumb.R
import com.example.bithumb.data.exchange.ExchangeSpinner
import com.example.bithumb.data.exchange.ExchangeAdapter
import com.example.bithumb.data.exchange.ExchangeData
import com.example.bithumb.data.remote.BitService
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var payment_currency = Constants.PAYMENT_CURRENCY_KRW
    private var order_currency = Constants.ORDER_CURRENCY
    private var exchange_rate = 1F

    /** spinner */
    private var mExchangeSpinnerList : ArrayList<ExchangeSpinner> = arrayListOf()
    private lateinit var mExchangeAdapter: ExchangeAdapter

    /** Viewpager & adpater */
    var mFragmentAdapter = FragmentAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** container = ViewPager */
        setupViewPager()
        /** tabs = TapLayout */
        tabs.setupWithViewPager(container)
        /** spinner init */
        initSpinner()

    }

    /** Tab( fragment ) setting */
    fun setupViewPager(){
        mFragmentAdapter.addFragment(FragmentTicker(), "현재가")
        mFragmentAdapter.addFragment(FragmentOrder(), "거래 현황")
        mFragmentAdapter.addFragment(ThirdFragment(), "etc")
        container.adapter = mFragmentAdapter
        container?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                val fragment = mFragmentAdapter.getItem(position)

//                val bundle = Bundle()
//                bundle.putString("param1",payment_currency)
//                bundle.putString("param2",exchange_rate.toString())
//                fragment.arguments = bundle
//                mFragmentAdapter.replaceFragment(position,fragment)

            }

            override fun onPageScrollStateChanged(state: Int) {


            }
        })
    }

    fun initSpinner(){
        // TODO 1. spinner에서 표현할 데이터(mExchangeList : ArrayList<ExchangeRate>) 초기화
        //  spinner에 보여질 Data(text,image)
        mExchangeSpinnerList.add(ExchangeSpinner(Constants.PAYMENT_CURRENCY_KRW,R.drawable.won))
        mExchangeSpinnerList.add(ExchangeSpinner(Constants.PAYMENT_CURRENCY_USD,R.drawable.dollar))
        mExchangeSpinnerList.add(ExchangeSpinner(Constants.PAYMENT_CURRENCY_JPY,R.drawable.yen))

        // TODO 2. Adpater 초기화
        //  Spinner Data로 Adapter 생성
        mExchangeAdapter = ExchangeAdapter(this, mExchangeSpinnerList)

        // TODO 3. Spinner.adpater 초기화
        exSpinner.adapter = mExchangeAdapter

        // TODO 4. Spinner.onItemSelectedListener 정의
        exSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"아무것도 클릭 안됨", Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val clickedItem : ExchangeSpinner = parent?.getItemAtPosition(position) as ExchangeSpinner
                payment_currency = clickedItem.exchangeText // => Exchange Instance!!
                /** 변경된 환율 데이터 요청 */
                /* 통신: 비동기 => spinner에서 fragment로 환율 데이터 전달x => 데이터 수신부에서 처리 */
                getExchangeRate(payment_currency)

                Toast.makeText(applicationContext, "$payment_currency 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }



    /**
     * getExchangeRate
     * @param none
     * @return none
     *  1. retrofit client 생성한다. ( 데이터 통신을 위한 retrofit이 이미 object로 사용되므로.. )
     *  2. 한미일 환율 데이터를 가져온다( List<ExchangeRate> , https://api.manana.kr/ ... )
     *  3. 환율 변수 exchangeRateXXX 에 수신 데이터 값으로 초기화(변경) 한다.(Activity의 클래스 변수)
     */
    private fun getExchangeRate(rate:String){
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

        retrofit.create(BitService::class.java).getExchangeRate(rate)?.enqueue(object : retrofit2.Callback<List<ExchangeData>>{
            override fun onResponse(
                call: Call<List<ExchangeData>>,
                response: Response<List<ExchangeData>>
            ) {
                response.body()?.get(0)?.rate?.let { exchange_rate = it }

                val bundle = Bundle()
                bundle.putString("param1",payment_currency)
                bundle.putString("param2",exchange_rate.toString())
                mFragmentAdapter.setFragmentExchange(bundle)


//                (container.adapter as FragmentAdapter).replaceFragment(container.currentItem,fragment)


            }

            override fun onFailure(call: Call<List<ExchangeData>>, t: Throwable) {
                Log.d("MainActivity","Exchange err"+t.message)
            }
        })
    }
}