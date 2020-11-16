package com.example.booksearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksearch.data.BookItem
import com.example.booksearch.util.RetrofitUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var total : Double = 0.0
    private var startCnt = 1
    private val displayCnt = 20

    private var listData : ArrayList<BookItem> = ArrayList()

    private lateinit var linearLayoutManager : LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var myAdapter : BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        book_rv.layoutManager = linearLayoutManager
        // adapter
        myAdapter = BookAdapter(this,listData)
        book_rv.adapter = myAdapter

//        RetrofitUtils.getBitService().searchBookItem("공지영"/*,0,3*/).enqueue(object : retrofit2.Callback<ResponseBody>{
        btn_search.setOnClickListener {
            inputData.text.toString()?.let{
                bookSearch(it,startCnt,displayCnt)
            }
        }
//        var onScrollListener : RecyclerView.OnScrollListener = RecyclerView.OnScrollListener(object : RecyclerView.OnScrollListener->
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

    private fun bookSearch(query:String,start : Int, display:Int){
        RetrofitUtils.getBookService().searchBookItem(query,start,display).enqueue(object : retrofit2.Callback<HashMap<String, Any>>{
            override fun onResponse(call: Call<HashMap<String, Any>>, response: Response<HashMap<String, Any>>) {
                if (response.isSuccessful && response.body()!=null){
                    // TODO : 전체 데이터 개수 확인
                    total = response.body()?.get("total") as Double
                    listData.clear()
                    response.body()?.get("items").let {items->
                        Log.d("MainActivity_Response_S","items.toString: "+items.toString())
                        if(/*items is LinkedHashMap<*,*> || */ items is ArrayList<*>){
                            items.forEach {item->
                                if (item is Map<*,*>){
                                    Log.d("MainActivity_Response_S", "item.toString: ${item["title"]}")
                                    var data = BookItem(
                                        // 태그 제거
                                        // TODO : 정규표현식 학습하기
                                        item["title"].toString().replace("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),""),
                                        item["publisher"].toString().replace("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),""),
                                        item["author"].toString().replace("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>".toRegex(),""),
                                        item["price"].toString(),
                                        item["image"].toString(),
                                        item["link"].toString(),
                                    )
                                    listData.add(data)
                                    myAdapter.addItems(listData)
                                    // 전체 데이터 갱신(view)
                                    myAdapter.notifyDataSetChanged()
                                }
                            }
                            startCnt += items.size
                        }
                    }
//                    if(listData.size>0){
//                        Log.d("MainActivity_Response_S", "listData.size: ${listData.size}")
//                        var bookTitle : String = ""
//                        listData.forEachIndexed { index, bookItem ->
//                            bookTitle =bookTitle + index.toString() + ". "+  bookItem.title + "\n"
//                        }
//                        textview.text = bookTitle
//                    }
                }
                Log.d("MainActivity_Response_S", "isSuccessful!!: ${response.isSuccessful}")
                Log.d("MainActivity_Response_S","body: "+response.body())
//                Log.d("MainActivity_Response_S","body.string(): "+response.body()?.string())
                Log.d("MainActivity_Response_S","body.toString(): "+response.body().toString())
                Log.d("MainActivity_Response_S","errorBody.toString: "+response.errorBody().toString())
                Log.d("MainActivity_Response_S","errorBody.string: "+response.errorBody()?.string())
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                Log.d("MainActivity_Response_F","Failure!!: "+t.message)
            }
        })
    }
}