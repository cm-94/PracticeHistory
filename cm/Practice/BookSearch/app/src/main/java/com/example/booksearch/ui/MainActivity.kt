package com.example.booksearch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booksearch.R
import com.example.booksearch.data.BookData
import com.example.booksearch.data.BookItem
import com.example.booksearch.data.BookLink
import com.example.booksearch.ui.adpater.BookAdapter
import com.example.booksearch.util.CommonUtils
import com.example.booksearch.util.RetrofitUtils
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    // 검색된 전체 데이터 수
    private var total : Double = 0.0
    // 전체 데이터 중 요청 보낼 시작값
    private var startCnt = 1
    // 한번에 요청 할 데이터 수
    private val displayCnt = 5
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
                    if (startCnt < total && startCnt < CommonUtils.DISPLAY_MAX) {
                        searchBook(true)
                    }
                }// 최대 데이터 조회
            }
        })
        // 검색 버튼 클릭 시
        btn_search.setOnClickListener {
            // 화면 최상단으로 이동
            book_rv.scrollToPosition(0)
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
                bookAdapter.notifyDataSetChanged()
                startCnt = 1
                // 사용자 입력값 확인
                editInput = it
                // 공백 : 데이터 요청 x & RecyclerView item => 0개로 초기화
                if (editInput == ""){
                    Toast.makeText(applicationContext,getString(R.string.input_search),Toast.LENGTH_SHORT).show()
                    return
                }// 데이터 요청 : 검색어(input)에 대해 첫번째부터 displayCnt(20)개 만큼 요청
            }
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
            retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.body() != null && response.isSuccessful){
                    val res = response.body()?.string()
                    val gson = Gson()
                    val jsonReader = JsonReader(StringReader(res))

                    val bookData : BookData = gson.fromJson(jsonReader,BookData::class.java)

                    listData.addAll(bookData.getBookItems())

                    progressBar.visibility = View.GONE
                    bookAdapter.notifyDataSetChanged()
                    bRequest = true
                }

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
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 검색 Progressbar -> GONE
                progressBar.visibility = View.GONE
                // 검색 Flag => true 변경
                bRequest = true
            }
        })
    }



    var data = "{\n" +
            "    \"lastBuildDate\": \"Mon, 23 Nov 2020 20:37:07 +0900\",\n" +
            "    \"total\": 8139,\n" +
            "    \"start\": 1,\n" +
            "    \"display\": 5,\n" +
            "    \"items\": [\n" +
            "    {\n" +
            "    \"title\": \"채식주의자 (<b>한강</b> 연작소설,맨부커 인터내셔널 수상작)\",\n" +
            "    \"link\": \"http://book.naver.com/bookdb/book_detail.php?bid=3309417\",\n" +
            "    \"image\": \"https://bookthumb-phinf.pstatic.net/cover/033/094/03309417.jpg?type=m1&udate=20200904\",\n" +
            "    \"author\": \"<b>한강</b>\",\n" +
            "    \"price\": \"12000\",\n" +
            "    \"discount\": \"10800\",\n" +
            "    \"publisher\": \"창비\",\n" +
            "    \"pubdate\": \"20071030\",\n" +
            "    \"isbn\": \"8936433598 9788936433598\",\n" +
            "    \"description\": \"표제작인 『채식주의자』는 지금까지 소설가 <b>한강</b>이 발표해온 작품에 등장하였던 욕망,식물성,죽음 등 인간 본연의 문제들을 한 편에 집약해 놓은 수작이라고 평가받는다.&#x0D;&#x0D;『채식주의자』는 육식을 거부하는 영혜를 바라보는 그의 남편 '나'의 이야기이다. '영혜'는 작가가 10년전에 발표한 단편... \"\n" +
            "    \n" +
            "    },\n" +
            "    {\n" +
            "    \"title\": \"<b>한강</b>을 따라 흐르는 고대사를 찾아서 3\",\n" +
            "    \"link\": \"http://book.naver.com/bookdb/book_detail.php?bid=17467387\",\n" +
            "    \"image\": \"https://bookthumb-phinf.pstatic.net/cover/174/673/17467387.jpg?type=m1&udate=20201117\",\n" +
            "    \"author\": \"오순제\",\n" +
            "    \"price\": \"10000\",\n" +
            "    \"discount\": \"\",\n" +
            "    \"publisher\": \"수동예림\",\n" +
            "    \"pubdate\": \"20201101\",\n" +
            "    \"isbn\": \"1190197707 9791190197700\",\n" +
            "    \"description\": \"역사학자 오순제 박사의 인문학적 시각으로 새로 읽는 <b>한강</b>의 흐름과 역사적 맥락인 『<b>한강</b>을 따라 흐르는 고대사를 찾아서 3』. <b>한강</b> 편은 총 3권으로 이루어졌으며 3권은 남<b>한강</b> 편으로 북<b>한강</b>과 남<b>한강</b>이 만나는 두물머리인 양평군과 남양주시, 하남시, 구리시, 서울특별시, 고양시, 파주시, 김포시, 강화군의... \"\n" +
            "    \n" +
            "    },\n" +
            "    {\n" +
            "    \"title\": \"<b>한강</b>을 따라 흐르는 고대사를 찾아서 2\",\n" +
            "    \"link\": \"http://book.naver.com/bookdb/book_detail.php?bid=17467394\",\n" +
            "    \"image\": \"https://bookthumb-phinf.pstatic.net/cover/174/673/17467394.jpg?type=m1&udate=20201117\",\n" +
            "    \"author\": \"오순제\",\n" +
            "    \"price\": \"10000\",\n" +
            "    \"discount\": \"\",\n" +
            "    \"publisher\": \"수동예림\",\n" +
            "    \"pubdate\": \"20201101\",\n" +
            "    \"isbn\": \"1190197693 9791190197694\",\n" +
            "    \"description\": \"역사학자 오순제 박사의 인문학적 시각으로 새로 읽는 <b>한강</b>의 흐름과 역사적 맥락인 『<b>한강</b>을 따라 흐르는 고대사를 찾아서 2』. <b>한강</b> 편은 총 3권으로 이루어졌으며 2권은 남<b>한강</b> 편으로 <b>한강</b> 본류인 남<b>한강</b>은 강원도 태백시 창죽동 금대봉(金臺峰)의 북서쪽 계곡의 고목나무샘에서 발원하여 검룡소(儉龍沼)를... \"\n" +
            "    \n" +
            "    },\n" +
            "    {\n" +
            "    \"title\": \"<b>한강</b>을 따라 흐르는 고대사를 찾아서 1\",\n" +
            "    \"link\": \"http://book.naver.com/bookdb/book_detail.php?bid=17467395\",\n" +
            "    \"image\": \"https://bookthumb-phinf.pstatic.net/cover/174/673/17467395.jpg?type=m1&udate=20201117\",\n" +
            "    \"author\": \"오순제\",\n" +
            "    \"price\": \"10000\",\n" +
            "    \"discount\": \"\",\n" +
            "    \"publisher\": \"수동예림\",\n" +
            "    \"pubdate\": \"20201101\",\n" +
            "    \"isbn\": \"1190197685 9791190197687\",\n" +
            "    \"description\": \"역사학자 오순제 박사의 인문학적 시각으로 새로 읽는 <b>한강</b>의 흐름과 역사적 맥락인 『<b>한강</b>을 따라 흐르는 고대사를 찾아서 1』. <b>한강</b> 편은 총 3권으로 이루어졌으며 1권은 북<b>한강</b> 편으로 북<b>한강</b>은 북한의 금강산 부근에서 발원한 금강천이 남쪽으로 흐르면서 강원도 김화군에서 금성천을 합친 후, 휴전선을 지나... \"\n" +
            "    \n" +
            "    },\n" +
            "    {\n" +
            "    \"title\": \"오! <b>한강</b> 세트 (전5권)\",\n" +
            "    \"link\": \"http://book.naver.com/bookdb/book_detail.php?bid=14788734\",\n" +
            "    \"image\": \"https://bookthumb-phinf.pstatic.net/cover/147/887/14788734.jpg?type=m1&udate=20190501\",\n" +
            "    \"author\": \"김세영\",\n" +
            "    \"price\": \"60000\",\n" +
            "    \"discount\": \"54000\",\n" +
            "    \"publisher\": \"가디언\",\n" +
            "    \"pubdate\": \"20190425\",\n" +
            "    \"isbn\": \"1189159279 9791189159276\",\n" +
            "    \"description\": \"<b>한강</b>』!민주화 시위가 치열했던 1980년대 말, 해방 이후부터 1987년 6월 항쟁까지 우리의\" }]}"
}


/*
InfoActivity - viewpager에서 좌우 스크롤 시 수신 데이터 이상 넘어가는 오류
 - MainActivity & BookAdapter(RecyclerView Adapter) 에서 BookLink(object).addItem 중복 확인 후 수정
 - 수신 데이터만큼 좌우 스크롤 정상 확인
 -
 */