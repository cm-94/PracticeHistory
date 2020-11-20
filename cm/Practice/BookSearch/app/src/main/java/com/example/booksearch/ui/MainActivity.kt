package com.example.booksearch.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearch.R
import com.example.booksearch.data.BookItem
import com.example.booksearch.data.BookLink
import com.example.booksearch.ui.adpater.BookAdapter
import com.example.booksearch.util.RetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // 검색된 전체 데이터 수
    private var total : Double = 0.0
    // 전체 데이터 중 요청 보낼 시작값
    private var startCnt = 1
    // 한번에 요청 할 데이터 수
    private val displayCnt = 20
    // 사용자 입력 값
    private var editInput : String = ""
    // 수신된 데이터(BookItem) 담을 리스트
    private var listData : MutableList<BookItem> = arrayListOf()

    // 데이터 요청 시 Flag
    private var bRequest : Boolean = true

    private lateinit var linearLayoutManager : LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var bookAdapter : BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    // TODO 데이터 저장!!
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initView(){
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        book_rv.layoutManager = linearLayoutManager
        // adapter
        bookAdapter = BookAdapter(this, listData)
        book_rv.adapter = bookAdapter
        book_rv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // 최하단 스크롤 시
                if (!book_rv.canScrollVertically(1)) {
                    if (startCnt < total || startCnt < 1001) {
                        searchBook(true)
                    }
                }// 최대 데이터 조회
            }
        })
        // 검색 버튼 클릭 시
        btn_search.setOnClickListener {
            // 화면 상단으로 이동
            book_rv.scrollToPosition(0)
            // Link 데이터 초기화
            BookLink.clear()
            // 책 검색
            searchBook(false)
        }
    }

    /**
     * 검색조건 초기화 & 데이터 요청
     * 입력값(EditText), 시작값(1), 기존 데이터(ListData) 초기화
     * @param scrollFlag  스크롤 이벤트로 호출 시 검색조건 초기화 x
     */
    private fun searchBook(scrollFlag: Boolean){
        if(!scrollFlag){
            edit_Input.text.toString().let{
                // 검색버튼은 클릭할 때마다 처음부터 데이터 요청!!
                listData.clear()
                startCnt = 1
                // 사용자 입력값 확인
                editInput = it
                // 공백 : 데이터 요청 x & RecyclerView item => 0개로 초기화
                if (editInput == ""){
                    bookAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext,getString(R.string.input_search),Toast.LENGTH_SHORT).show()
                    return
                }// 데이터 요청 : 검색어(input)에 대해 첫번째부터 displayCnt(20)개 만큼 요청
            }
            // 화면을 최상단으로 이동
        }
        if(bRequest){ // progressBar.visibility 상태와 같음!!
            requestBook(editInput, startCnt, displayCnt)
        }
    }

    /**
     * 검색 요청 함수
     */
    private fun requestBook(query: String, start: Int, display: Int){
        bRequest = false
        // 검색 Progressbar -> VISIBLE
        progressBar.visibility = View.VISIBLE
        RetrofitUtils.getBookService().searchBookItem(query, start, display).enqueue(object :
            retrofit2.Callback<HashMap<String, Any>> {
            override fun onResponse(
                call: Call<HashMap<String, Any>>,
                response: Response<HashMap<String, Any>>
            ) {
                // 검색 Progressbar -> GONE
                if (response.isSuccessful && response.body() != null) {
                    // 전체 데이터 개수 확인
                    total = response.body()?.get("total") as Double
                    response.body()?.get("items").let { items ->
                        if (items is ArrayList<*>) {
                            items.forEach { item ->
                                if (item is Map<*, *>) {
                                    // 가격 데이터 처리
                                    var price = 0
                                    if(item["price"].toString().isNotBlank()){
                                        price = item["price"].toString().split(".")[0].toInt()
                                    }
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
                                            price,
                                            item["image"].toString(),
                                            item["link"].toString(),
                                        )
                                    )
                                    BookLink.addLink(item["link"].toString())
                                }
                            }
                            startCnt += items.size
                        }
                    }
                    // 전체 데이터 갱신(view)
                    bookAdapter.notifyDataSetChanged()
                    // progressBar => Gone
                    progressBar.visibility = View.GONE
                    // 검색 Flag => true 변경
                    bRequest = true
                }
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                // 검색 Progressbar -> GONE
                progressBar.visibility = View.GONE
                // 검색 Flag => true 변경
                bRequest = true
            }
        })
    }
}


/*
InfoActivity - viewpager에서 좌우 스크롤 시 수신 데이터 이상 넘어가는 오류
 - MainActivity & BookAdapter(RecyclerView Adapter) 에서 BookLink(object).addItem 중복 확인 후 수정
 - 수신 데이터만큼 좌우 스크롤 정상 확인
 -
 */