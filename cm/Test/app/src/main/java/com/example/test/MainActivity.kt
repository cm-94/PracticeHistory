package com.example.test

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.ExpectAdapter
import com.example.test.adapter.TaxAdapter
import com.example.test.data.ExpectData
import com.google.android.material.tabs.TabLayout


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {
    // 메인 최상위 스크롤뷰
    var main_scroll_view : View? = null
    // 스크롤뷰에서 헤더로 쓰일 뷰
    var tab_ll : View? = null

    /** 임의 데이터 **/
    // 사용자이름
    var uName = "신희아빠"



    // Recycler View 초기화 할 LinearLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    // 예상 금융소득 Recycler View 에 표시해줄 Adapter
    private lateinit var expectAdapter : ExpectAdapter
    // 종합과세 Recycler View 에 표시해줄 Adapter
    private lateinit var taxAdapter : TaxAdapter
    // Adapter에서 View를 구성하는데 쓰일 ArrayList
    private var arrData : ArrayList<ExpectData> = ArrayList()
    private lateinit var expectLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO : 1. 탭 -> 스크롤뷰 상단 고정 세팅
        setTitle()

        // TODO : 2. 임의 데이터 생성 및 arrExpect에 추가
        setData()

        // TODO : 3. 예상 금융소득 & 종합과세 ( RecyclerView ) 세팅
        setExpectRecycler()
        setTaxRecycler()

        // TODO : 4. Spinner 세팅
        setSpinner()

    }

    private fun setExpectRecycler(){
        expectAdapter = ExpectAdapter(this, arrData)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var recyclerview = findViewById<RecyclerView>(R.id.expect_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = expectAdapter
//        recyclerview.isNestedScrollingEnabled = false

        expectAdapter.notifyDataSetChanged() // 갱신
    }

    private fun setTaxRecycler(){
        // 예금은 빼고 데이터 구성
        val arrTax : ArrayList<ExpectData> = ArrayList()
        for (arrDatum in arrData) {
            if(arrDatum.product_type != "2"){
                arrTax.add(arrDatum)
            }
        }

        taxAdapter = TaxAdapter(this, arrTax)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var recyclerview = findViewById<RecyclerView>(R.id.tax_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = taxAdapter
//        recyclerview.isNestedScrollingEnabled = false

        taxAdapter.notifyDataSetChanged() // 갱신
    }


    private fun setTitle(){
        main_scroll_view = findViewById<NewScrollView>(R.id.main_scroll_view)
        tab_ll = findViewById<TabLayout>(R.id.header_view)
        (main_scroll_view as NewScrollView?)?.setHeader(tab_ll)
    }

    private fun setData(){
//        for (i in 0..10){
//            val data = ExpectData(
//                product_name = "삼성전자",
//                product_type = "1",
//                product_price = "78500",
//                price_type = "1",
//                due_date = "2020.12.03"
//            )
//            arrExpect.add(data)
//        }
        arrData.add(ExpectData("삼성전자", "1", "78500", "1", "2020.12.03", "1400", "1.58","1000000"))
        arrData.add(ExpectData("SK하이닉스", "1", "750000", "1", "2020.12.03", "500", "0.68","1000000"))
        arrData.add(ExpectData("KODEX 미국 S&P 고배당 커버드콜","3","78500","3","2020.12.03","2500","2.25","1000000"))
        arrData.add(ExpectData("지급식증권투자신탁(주식형) 펀드", "3","750000", "3", "2020.12.03", "591", "2.80","1000000"))
        arrData.add(ExpectData("저축예금 결산이자", "2", "78500", "2", "2020.12.03", "0", "0","1000000"))
        arrData.add(ExpectData("현대차", "1", "138500", "1", "2020.12.03", "22000", "10.45","1000000"))

        // 이름 설정
        val user_name = findViewById<TextView>(R.id.txt_intro)

        user_name.text = getString(R.string.txt_intro, uName)
        val intro_txt = SpannableStringBuilder(user_name.text)            // 데이터(.1f) -> String으로
        intro_txt.setSpan(RelativeSizeSpan(1.5f),0,uName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // 이름 크기 ↑
        intro_txt.setSpan(StyleSpan(Typeface.BOLD),0,uName.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE) // Style : Bold
        user_name.text = intro_txt
    }

    /**
     * 과세표준 스피너 세팅 함수
     */
    private fun setSpinner(){
        val spinner: Spinner = findViewById(R.id.tax_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tax_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        }

        // 선택된 item -> Text색 검정으로 강제 지정
        // Darkmode 설정 시 흰색으로 나옴..
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                (parent.getChildAt(0) as TextView).setTextColor(Color.BLACK)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}



//    fun setData(){
//        for (i in 0..10){
//            val vi = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val v: View = vi.inflate(R.layout.expect_item, null)
//
//            val name =  v.findViewById(R.id.item_name) as TextView
//            val price =  v.findViewById(R.id.item_price) as TextView
//            val type =  v.findViewById(R.id.item_type) as TextView
//            val date =  v.findViewById(R.id.due_date) as TextView
//
//            // TODO : 데이터 각각 포맷팅 & style 변경 등
//            name.text = "삼성전자"
//            price.text = "78500"
//            type.text = "이자"
//            date.text = "2020.12.03"
//
//            // 데이터
//            expectLayout.addView(v, 0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
//        }
//        // 이름 설정
//        val user_name = findViewById<TextView>(R.id.txt_intro)
//        user_name.text = getString(R.string.txt_intro,"임창민")
//        // TODO 이름 크기 ↑ & bold 적용
//    }
