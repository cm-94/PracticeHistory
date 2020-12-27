package com.example.booksearch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearch.R
import com.example.booksearch.data.BookData
import com.example.booksearch.data.BookItem
import com.example.booksearch.ui.adpater.BookAdapter
import com.example.booksearch.util.Constants
import com.example.booksearch.util.RetrofitUtils
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.StringReader


class MainActivity : AppCompatActivity() {
    private val SAVED_TOTAL = "SAVED_TOTAL"
    private val SAVED_START = "SAVED_START"

    // 검색된 전체 데이터 수
    private var total = 0
    // 전체 데이터 중 요청 보낼 시작값
    private var startCnt = 1
    // 한번에 요청 할 데이터 수
    private val displayCnt = 20
    // 사용자 입력 값
    private var editInput : String = ""
    // 수신된 데이터(BookItem) 담을 리스트
    private var listData : ArrayList<BookItem> = arrayListOf()

    // 데이터 요청 시 Flag
    private var bRequest : Boolean = true

    private lateinit var linearLayoutManager : LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var bookAdapter : BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("LifeCycle_Main", "savedInstanceState: ${if(savedInstanceState == null) "null" else "NotNull"}")

        savedInstanceState?.let{ data ->
            // 저장된 List<BookItem>으로 listData 세팅
            data.getParcelableArrayList<BookItem>(Constants.BOOK_LIST)?.let { listData = it }
            // 저장된 검색어로 EditText 세팅
            data.getString(Constants.EDIT_INPUT)?.let{ it ->
                editInput = it
                edit_input.setText(it)
            }
            // 저장된 total & start로 세팅
            data.getInt(SAVED_TOTAL).let{ total = it }
            data.getInt(SAVED_START).let{ startCnt = it }
        }

        initView()
    }


    private fun initView(){
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        book_rv.layoutManager = linearLayoutManager
        // adapter 초기화
        bookAdapter = BookAdapter(this, listData) // => 앱 백그라운드 이후 화면 재진입 시 listData로 화면 세팅됨!!
        book_rv.adapter = bookAdapter
        book_rv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // 키보드 숨기기
                val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edit_input.windowToken, 0)
                // 최하단 스크롤 시
                if (!book_rv.canScrollVertically(1)) {
                    if (startCnt < total && startCnt < Constants.DISPLAY_MAX) {
                        searchBook(true)
                    }
                }// 최대 데이터 조회
            }
        })

        // 검색창에서 엔터 클릭 시
        edit_input.setOnEditorActionListener { p0, p1, p2 ->
            // 검색 실행
            searchBook(false)
            true
        }
        // RecyclerView 의 Item들을 구분짓는 Divider을 추가!!
//        val dividerItemDecoration = DividerItemDecoration(
//            book_rv.context,
//            linearLayoutManager.orientation
//        )
//        book_rv.addItemDecoration(dividerItemDecoration)
        // ListView : android:divider="#FF00000"
        //            android:dividerHeight="1dp"
        // 검색 버튼 클릭 시
        btn_search.setOnClickListener {
            // 화면 최상단으로 이동
            book_rv.scrollToPosition(0)
            // 책 검색
            searchBook(false)
        }

        // Added in API Level 1
        // Call this view's OnClickListener, if it is defined.
        // Performs all normal actions associated with clicking: reporting accessibility event, playing a sound, etc.
//        btn_search.performClick()

        // Added in API Level 15
        // Directly call any attached OnClickListener
        // Unlike performClick(), this only calls the listener, and does not do any associated clicking actions like reporting an accessibility event.
//        btn_search.callOnClick()
    }


    /**
     * 검색조건 초기화 & 데이터 요청
     * 입력값(EditText), 시작값(1), 기존 데이터(ListData) 초기화
     * @param scrollFlag  스크롤 이벤트로 호출 시 검색조건 초기화 x
     */
    private fun searchBook(scrollFlag: Boolean){
        if(!scrollFlag){
            // 검색버튼은 클릭할 때마다 처음부터 데이터 요청!!
            listData.clear()
            bookAdapter.notifyDataSetChanged()
            startCnt = 1
            // 사용자 입력값 확인
            edit_input.text.toString().let{
                editInput = it
                // 공백 : 데이터 요청 x & RecyclerView item => 0개로 초기화
                if (editInput == ""){
                    // 검색된 자료없음 Text -> Visible
                    no_book_data.visibility = View.VISIBLE
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.input_search),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }// 데이터 요청 : 검색어(input)에 대해 첫번째부터 displayCnt(20)개 만큼 요청
            }
        }
        if(bRequest){ // progressBar.visibility 상태와 같음!!
            requestBook(editInput, startCnt, displayCnt)
            // 데이터 요청할 때 키보드 내림!!
            val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edit_input.windowToken, 0)
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
            retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                // 검색된 자료없음 Text -> Gone
                no_book_data.visibility = View.GONE
                val res = response.body()?.string()
                if (res != null && response.isSuccessful) {
                    val gson = Gson()
                    val jsonReader = JsonReader(StringReader(res))

                    // 수신된 데이터(string)를 Object(BookData Class)로 변환!!
                    val bookData: BookData = gson.fromJson(jsonReader, BookData::class.java)
                    // 검색된 전체 데이터 숫자 초기화
                    total = bookData.total
                    // 수신된 책 데이터(list) 추가
                    listData.addAll(bookData.items)
                    // 새 시작 index로 세팅
                    startCnt += bookData.items.size

                    // progressBar => Gone
                    progressBar.visibility = View.GONE
                    bookAdapter.notifyDataSetChanged()

                    // 데이터 요청 가능하도록 검색 Flag -> true 변경!!
                    bRequest = true
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 검색 Progressbar -> GONE
                Log.d("LifeCycle_Main", "requestBook_Failure!!")
                progressBar.visibility = View.GONE
                // 데이터 요청 가능하도록 검색 Flag -> true 변경!!
                bRequest = true
            }
        })
    }

    override fun onStart() {
        Log.d("LifeCycle_Main", "onStart() 호출!!")
        super.onStart()
    }

    override fun onResume() {
        Log.d("LifeCycle_Main", "onResume() 호출!!")
        super.onResume()
    }

    override fun onPause() {
        Log.d("LifeCycle_Main", "onPause() 호출!!")
        super.onPause()
    }

    override fun onStop() {
        Log.d("LifeCycle_Main", "onStop() 호출!!")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("LifeCycle_Main", "onDestroy() 호출!!")
        super.onDestroy()
    }

    // 데이터 저장!!
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("LifeCycle_Main", "onSaveInstanceState() 호출!!")
        // 검색된 List<BookItem> 저장
        outState.putParcelableArrayList(Constants.BOOK_LIST, listData)
        // 현재 검색창 입력 값 저장
        outState.putString(Constants.EDIT_INPUT, edit_input.text.toString())
        // 현재 total & start 저장
        outState.putInt(SAVED_TOTAL, total)
        outState.putInt(SAVED_START, startCnt)

        Log.d("LifeCycle_Main", "outState: ${if(outState == null) "null" else "NotNull"}")
        super.onSaveInstanceState(outState)
    }
}



/*---------- HashMap으로 수신된 데이터 처리 과정 ----------*/
//                listData.add(bookItem)


// 검색 Progressbar -> GONE
//                if (response.isSuccessful && response.body() != null) {
//                    // 전체 데이터 개수 확인
//                    total = response.body()?.get("total") as Double
//                    response.body()?.get("items").let { items ->
//                        if (items is ArrayList<*>) {
//                            items.forEach { item ->
//                                if (item is Map<*, *>) {
//                                    // 가격 데이터 처리
//                                    var price = 0
//                                    if(item["price"].toString().isNotBlank()){
//                                        price = item["price"].toString().split(".")[0].toInt()
//                                    }
//                                    listData.add(
//                                        BookItem(
//                                            // 태그 제거
//                                            item["title"].toString().replace(
//                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
//                                                ""
//                                            ),
//                                            item["publisher"].toString().replace(
//                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
//                                                ""
//                                            ),
//                                            item["author"].toString().replace(
//                                                "<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),
//                                                ""
//                                            ),
//                                            // 가격 "" => 0 으로 초기화
//                                            price,
//                                            item["image"].toString(),
//                                            item["link"].toString(),
//                                        )
//                                    )
//                                    BookLink.addLink(item["link"].toString())
//                                }
//
//                                var gson :Gson = Gson()
//                                var jsonReader : JsonReader = JsonReader(StringReader(item.toString()))
//                                jsonReader.isLenient = true
//                                Log.d("Receive_data","item.toString(): $item")
//                                Log.d("item.toString()","JsonReader: $jsonReader.")
//                                var bookItem : BookItem = gson.fromJson(jsonReader,BookItem::class.java)
////                                val book = gson.fromJson(jsonReader,BookItem::class)
////                                listData.add(bookItem)
//
//                            }
//                            startCnt += items.size
//                        }
//                    }
//                    // 전체 데이터 갱신(view)
//                    bookAdapter.notifyDataSetChanged()
//                    // progressBar => Gone
//                    progressBar.visibility = View.GONE
//                    // 검색 Flag => true 변경
//                    bRequest = true
//                }