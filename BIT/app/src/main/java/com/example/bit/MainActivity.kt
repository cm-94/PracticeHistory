package com.example.bit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bit.Utils.RetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // myAdapter -> MainActivity의 recycler View에 아이템을 넣어 띄울 MainAdapter 클래스
    var myAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO : 1. 전체 데이터 수신(ticker/ALL_KRW)
        /** 전체 버튼 클릭 리스너 등록 **/
        showAllButton.setOnClickListener {
            showAllTickerData()
        }
    }

    /**
     * showAllData()
     * @param none
     * @return none
     *  - 전체(110개) Ticker(현재 시세) 데이터 수신
     *  - myAdapter에 110개 데이터를 담아 recycler view에 표시
     */
    private fun showAllTickerData(){
        // RetrofitUtils.getBitService(applicationContext) -> 톹신에 사용될 client 생성 및 BitService return
        // BTCTicker().BTCTicker() -> BTC 종목에 대한 Ticker 데이터를 받아온다. => @GET, URI : .../ticker/BTC_KRW.
        // enqueue( ... ) -> 해당 URL로 요청을 보낸다
        // object : retrofit2.Callback<ResponseBody> { ... } -> 전달 받은 응답(CallBack)에 대한 처리
        RetrofitUtils.getBitService(applicationContext).BTCTicker()?.enqueue(object : retrofit2.Callback<ResponseBody> {
            // 요청이 성공했을 경우(서버에 요청이 전달 된 상태)
            override fun onResponse(call: Call<ResponseBody?>?,response: Response<ResponseBody?>) {
                // 정상 Callback을 받은 경우
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Response Data", response.body()!!.string())
                    // TODO : 1.1 응답받은 전체 데이터를 MainAdapter에 담아 화면에 보여주기
                    //  + MainAdapter 수정 필요 사항
                    //   1. MainAdapter에서 쓰일 items( MutableList<TickerData> )의 데이터 타입 수정
                    //   2. addItems()의 param(데이터) 수정
                    //myAdapter.addItems(/** RecyclerView에 표현할 MutableList<MyData> 넣기!! **/)

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
                call: Call<ResponseBody?>?, t: Throwable ) {
                Log.d("MainActivity", "Connect_Error")
                t.printStackTrace()
            }
        })
    }


}

