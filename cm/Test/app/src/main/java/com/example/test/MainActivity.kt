package com.example.test

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {
    var main_scroll_view : View? = null
    var tab_ll : View? = null

    // Recycler View 초기화 할 LinearLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    // arr(Data)를 Recycler View에 표시해줄 Adapter
    private lateinit var myAdapter : ExpectAdapter
    // Adapter에서 View를 구성하는데 쓰일 ArrayList
    private var arrExpect : ArrayList<ExpectData> = ArrayList()
    private lateinit var expectLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        myAdapter = ExpectAdapter(this,arrExpect)
//        myAdapter.notifyDataSetChanged()
//        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
//        var recyclerview = findViewById<RecyclerView>(R.id.expect_recyclerview)
//        recyclerview.layoutManager = linearLayoutManager
//        // adapter
//        recyclerview.adapter = myAdapter
//        setData()
//        myAdapter.notifyDataSetChanged()

        expectLayout = findViewById(R.id.expect_area)
        setData() //
        setupViewPager()
    }

    fun setData(){
        for (i in 0..10){
            val vi = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View = vi.inflate(R.layout.expect_item, null)
            
            val name =  v.findViewById(R.id.item_name) as TextView
            val price =  v.findViewById(R.id.item_price) as TextView
            val type =  v.findViewById(R.id.item_type) as TextView
            val date =  v.findViewById(R.id.due_date) as TextView

            // TODO : 데이터 각각 포맷팅 & style 변경 등
            name.text = "삼성전자"
            price.text = "78500"
            type.text = "이자"
            date.text = "2020.12.03"

            // 데이터
            expectLayout.addView(v, 0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
        }

        // 이름 설정
        val user_name = findViewById<TextView>(R.id.txt_intro)
        user_name.text = getString(R.string.txt_intro,"임창민")
        // TODO 이름 크기 ↑ & bold 적용


    }



    fun setupViewPager(){
        main_scroll_view = findViewById<NewScrollView>(R.id.main_scroll_view)
        tab_ll = findViewById<TabLayout>(R.id.header_view)
        (main_scroll_view as NewScrollView?)?.setHeader(tab_ll)
    }

//    fun setData(){
//        for (i in 0..10){
//            var data = ExpectData(
//                product_name = "삼성전자",
//                product_type = "1",
//                product_price = "78500",
//                price_type = "1",
//                due_date = "2020.12.03"
//            )
//            arrExpect.add(data)
//        }
//    }


}