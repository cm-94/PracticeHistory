package com.example.booksearch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearch.data.BookItem
import com.example.booksearch.util.CommonUtils
import com.example.booksearch.util.RetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var total : Double = 0.0
    private var startCnt = 1
    private val displayCnt = 20

    private var inputText : String = ""

    private var listData : ArrayList<BookItem> = arrayListOf()

    private lateinit var linearLayoutManager : LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var myAdapter : BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        book_rv.layoutManager = linearLayoutManager

        // adapter
        myAdapter = BookAdapter(this, listData)
        book_rv.adapter = myAdapter
        book_rv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // 최하단 스크롤 시
                if (!book_rv.canScrollVertically(1)) {
                    if (startCnt < total || startCnt < 1001) {
                        Log.d("MainActivity_ScrollChange", "newState:$newState")
                        Log.d("MainActivity_ScrollChange", "total:$total, start:$startCnt")
                        bookSearch(inputText, startCnt, displayCnt)
                    }
                }
            }
        })

//        RetrofitUtils.getBitService().searchBookItem("공지영"/*,0,3*/).enqueue(object : retrofit2.Callback<ResponseBody>{
        btn_search.setOnClickListener {
            inputData.text.toString().let{
                // 검색버튼은 클릭할 때마다 처음부터 데이터 요청!!
                listData.clear()
                startCnt = 1
                // 사용자 입력값 확인
                inputText = it
                // 공백 : 데이터 요청 x & RecyclerView item => 0개로 초기화
                if (inputText == ""){
                    myAdapter.notifyDataSetChanged()
                }// 데이터 요청 : 검색어(input)에 대해 첫번째부터 displayCnt(20)개 만큼 요청
                else{
                    bookSearch(inputText, startCnt, displayCnt)
                }
            }
        }
//
//
//        )
//            RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                int totalItemCount = layoutManager.getItemCount();
//                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
//
//                if (lastVisible >= totalItemCount - 1) {
//                Log.d(TAG, "lastVisibled");
//                }
//            }
//        };
    }

    private fun bookSearch(query: String, start: Int, display: Int){
        // 검색 Progressbar -> VISIBLE
        progressBar.visibility = View.VISIBLE
        RetrofitUtils.getBookService().searchBookItem(query, start, display).enqueue(object :
            retrofit2.Callback<HashMap<String, Any>> {
            override fun onResponse(
                call: Call<HashMap<String, Any>>,
                response: Response<HashMap<String, Any>>
            ) {
                // 검색 Progressbar -> GONE
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    // TODO : 전체 데이터 개수 확인
                    total = response.body()?.get("total") as Double

                    response.body()?.get("items").let { items ->
                        Log.d("MainActivity_Response_S", "items.toString: " + items.toString())
                        Log.d("MainActivity_Response_S", "items.size: " + (items as ArrayList<*>).size)
                        Log.d("MainActivity_Response_S", "listData.size: " + listData.size)
                        if (/*items is LinkedHashMap<*,*> || */ items is ArrayList<*>) {
                            items.forEach { item ->
                                if (item is Map<*, *>) {
                                    Log.d("MainActivity_Response_S", "item.toString: ${item["title"]}")
                                    listData.add(BookItem(
                                        // 태그 제거
                                        // TODO : 정규표현식 학습하기
                                        item["title"].toString().replace("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(), ""),
                                        item["publisher"].toString(),
                                        item["author"].toString().replace("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(), ""),
                                        if(item["price"].toString().isNotBlank()) item["price"].toString().toInt() else 0,
                                        item["image"].toString(),
                                        item["link"].toString(),
                                    ))

                                    Log.d("MainActivity_BindAdd", "myAdapter.getItemCount: " + myAdapter.getItemCount())

                                }
                            }
                            startCnt += items.size
                        }
                    }
                    Log.d("MainActivity_Response_S", "listData.size2: " + listData.size)
                    myAdapter.addItems(listData)
                    // 전체 데이터 갱신(view)
                    myAdapter.notifyDataSetChanged()

                }
            }
            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                // 검색 Progressbar -> GONE
                progressBar.visibility = View.GONE
            }
        })
    }
}