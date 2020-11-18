package com.example.booksearch.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearch.R
import com.example.booksearch.data.BookItem
import com.example.booksearch.ui.adpater.BookAdapter
import com.example.booksearch.util.RetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var total : Double = 0.0
    private var startCnt = 1
    private val displayCnt = 20

    private var editInput : String = ""

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
                        requestSearch(editInput, startCnt, displayCnt)
                    }
                }
            }
        })

        edit_Input.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == KeyEvent.KEYCODE_ENTER) {
                    setSearch()
                }
                return true
            }
        })

//
//        edit_Input.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
//                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
//                    // 키보드 제거
//                    val imm: InputMethodManager = getSystemService(
//                        Context.INPUT_METHOD_SERVICE
//                    ) as InputMethodManager
//                    imm.hideSoftInputFromWindow(edit_Input.windowToken, 0)
//                    setSearch()
//                    return true
//                }
//                return false
//            }
//        })

        btn_search.setOnClickListener {
            setSearch()
        }
    }


    /**
     * 검색조건 초기화 & 데이터 요청
     * 입력값(EditText), 시작값(1), 기존 데이터(ListData) 초기화
     */
    private fun setSearch(){
        edit_Input.text.toString().let{
            // 검색버튼은 클릭할 때마다 처음부터 데이터 요청!!
            listData.clear()
            startCnt = 1
            // 사용자 입력값 확인
            editInput = it
            // 공백 : 데이터 요청 x & RecyclerView item => 0개로 초기화
            if (editInput == ""){
                myAdapter.notifyDataSetChanged()
            }// 데이터 요청 : 검색어(input)에 대해 첫번째부터 displayCnt(20)개 만큼 요청
            else{
                requestSearch(editInput, startCnt, displayCnt)
            }
        }
    }

    /**
     * 검색 요청 함수
     */
    private fun requestSearch(query: String, start: Int, display: Int){
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
                        Log.d(
                            "MainActivity_Response_S",
                            "items.size: " + (items as ArrayList<*>).size
                        )
                        Log.d("MainActivity_Response_S", "listData.size: " + listData.size)
                        if (/*items is LinkedHashMap<*,*> || */ items is ArrayList<*>) {
                            items.forEach { item ->
                                if (item is Map<*, *>) {
                                    Log.d(
                                        "MainActivity_Response_S",
                                        "item.toString: ${item["title"]}"
                                    )
                                    listData.add(
                                        BookItem(
                                            // 태그 제거
                                            item["title"].toString().replace(
                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
                                                ""
                                            ),
                                            item["publisher"].toString().replace(
                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
                                                ""
                                            ),
                                            item["author"].toString().replace(
                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
                                                ""
                                            ),
                                            // 가격 "" => 0 으로 초기화
                                            if (item["price"].toString()
                                                    .isNotBlank()
                                            ) item["price"].toString().toInt() else 0,
                                            item["image"].toString(),
                                            item["link"].toString(),
                                        )
                                    )

                                    Log.d(
                                        "MainActivity_BindAdd",
                                        "myAdapter.getItemCount: " + myAdapter.getItemCount()
                                    )

                                }
                            }
                            startCnt += items.size
                        }
                    }
                    Log.d("MainActivity_Response_S", "listData.size2: " + listData.size)
                    Toast.makeText(
                        applicationContext,
                        "총 데이터 : ${myAdapter.itemCount} ",
                        Toast.LENGTH_SHORT
                    ).show()
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