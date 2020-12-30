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
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.ExpectAdapter
import com.example.test.adapter.MajorAdapter
import com.example.test.adapter.TaxAdapter
import com.example.test.data.ExpectData
import com.example.test.data.MajorData
import com.google.android.material.tabs.TabLayout
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {
    /** 임의 데이터 **/
    private val dataFormat = DecimalFormat("#,###.#")


    // 사용자이름
    private val uName = "신희아빠"

    // (누적) 금융소득
    private val total_income = "5900172"
    // 손익통산
    private val total_prof = "0"


    // Adapter에서 View를 구성하는데 쓰일 ArrayList
    private var arrData : ArrayList<ExpectData> = ArrayList()
    private var arrTax : ArrayList<ExpectData> = ArrayList()
    private var arrMajor : ArrayList<MajorData> = ArrayList()
    private var arrMajorFore : ArrayList<MajorData> = ArrayList()

    /** View **/
    // 메인 최상위 스크롤뷰
    var main_scroll_view : View? = null
    // 스크롤뷰에서 헤더로 쓰일 뷰
    var tab_ll : View? = null


    // 오늘 날짜 TextView
    private lateinit var today_tv : TextView
    // 상단 초기화 버튼 btn_resetImageView
    private lateinit var btn_reset : ImageView

    // 상단 금융소득 TextView
    private lateinit var total_income_tv : TextView
    // 상단 손익통산 TextView
    private lateinit var total_prof_tv : TextView

    // 하단 누적 금융소득 TextView
    private lateinit var tab_total_income_tv : TextView


    // 대주주 해당 기준 날짜
    private lateinit var txt_major_state_out : TextView
    // 대주주 해당 없음 레이아웃
    private lateinit var major_out_ll : LinearLayout

    // 대주주 해당 기준 날짜
    private lateinit var txt_major_standard_date : TextView
    // 대주주 요건 충족 기준 날짜
    private lateinit var txt_major_fore_date : TextView

    // 대주주요건 확장/축소 버튼 ( ImageView )
    private lateinit var btn_info_major_expand : ImageView

    // 대주주 요건 상세 레이아웃
    private lateinit var major_info_ll : LinearLayout
    // 대주주 해당 레이아웃
    private lateinit var major_ll : RelativeLayout
    // 대주주 요건 충족 레이아웃
    private lateinit var major_fore_ll : RelativeLayout


    // Recycler View 초기화 할 LinearLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager

    // 예상 금융소득 Recycler View 에 표시해줄 Adapter
    private lateinit var majorAdapter : MajorAdapter
    // 종합과세 Recycler View 에 표시해줄 Adapter
    private lateinit var majorForeAdapter : MajorAdapter
    // 예상 금융소득 Recycler View 에 표시해줄 Adapter
    private lateinit var expectAdapter : ExpectAdapter
    // 종합과세 Recycler View 에 표시해줄 Adapter
    private lateinit var taxAdapter : TaxAdapter


    // 예상 금융소득 EditText
    private lateinit var edit_expect_income : EditText
    // 예상 금융소득 x 버튼
    private lateinit var btn_delete_exp : TextView


    // 대주주요건 충족 여부
    private var bMajor = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // TODO : 1. 탭 -> 스크롤뷰 상단 고정 세팅
        setHeaderView()

        // TODO : 2. 임의 데이터 생성 및 arrExpect에 추가
        setData()

        // TODO : 3. View 초기화
        initView()

        // TODO : 4. 대주주 해당 & 대주주 요건 충족 & 예상 금융소득 & 종합과세 ( RecyclerView ) 세팅
        setMajorRecycler()
        setMajorForeRecycler()

        setExpectRecycler()
        setTaxRecycler()

        // TODO : 5. Spinner 세팅
        setSpinner()
    }

    private fun initView(){
        // 날짜 세팅
        setDate()

        // 상단 초기화 버튼
        btn_reset = findViewById(R.id.btn_reset)
        btn_reset.setOnClickListener {
            // 1. 날짜 초기화
            setDate()
            // 2. 데이터 요청 보내기
            // 현재 : 임시데이터 -> 처리 x
        }

        // 상단 금융소득 세팅
        total_income_tv = findViewById(R.id.total_income)
        total_income_tv.text = dataFormat.format(total_income.toInt()).toString()
        // 하단(탭) 금융소득 세팅
        tab_total_income_tv = findViewById(R.id.tab_total_income)
        tab_total_income_tv.text = dataFormat.format(total_income.toInt()).toString()
        // 손익통산 세팅
        total_prof_tv = findViewById(R.id.total_prof)
        total_prof_tv.text = dataFormat.format(total_prof.toInt()).toString()

        // 대주주요건 상단(해당 주식 없음) 이름설정
        txt_major_state_out = findViewById(R.id.txt_major_state_out)
        txt_major_state_out.text = getString(R.string.txt_major_state_out, uName)

        val major_count = if(arrMajor.size == 0) 0 else arrMajor.size
        val major_fore_count = if(arrMajorFore.size == 0) 0 else arrMajorFore.size

        val major_require = findViewById<TextView>(R.id.major_require)
        major_require.text = getString(R.string.major_require,major_count,major_fore_count)

        // 대주주 해당 없음 레이아웃
        major_out_ll = findViewById(R.id.major_out_ll)
        // 대주주 해당  레이아웃
        major_ll = findViewById(R.id.major_ll)
        // 대주주 요건 충족 레이아웃
        major_fore_ll = findViewById(R.id.major_fore_ll)

        major_info_ll = findViewById(R.id.major_info_ll)

        // 확장 축소 버튼
        btn_info_major_expand = findViewById(R.id.btn_info_major_expand)
        btn_info_major_expand.setOnClickListener {
            val aniView : View
            if(arrMajor.size == 0 && arrMajorFore.size == 0){
                aniView = major_out_ll
            }else{
                if(arrMajor.size == 0){
                    major_ll.visibility = View.GONE
                }else if(arrMajorFore.size == 0){
                    major_fore_ll.visibility = View.GONE
                }

                aniView = major_info_ll
            }
            if(bMajor){
                // 화면 열기
                expand(aniView)
                // 상세정보 열림 플래그 변경
                bMajor = false
                // 살세정보 버튼 이미지 변경
                btn_info_major_expand.setImageResource(R.drawable.ic_arrow_up)
            }else{
                // 화면 닫기
                collapse(aniView)
                // 상세정보 열림 플래그 변경
                bMajor = true
                // 살세정보 버튼 이미지 변경
                btn_info_major_expand.setImageResource(R.drawable.ic_arrow_down)
            }
        }

        // 사용자 이름 설정
        val user_name_info = findViewById<TextView>(R.id.txt_intro)                 // 화면 상단 이름설정
        val user_name_major_in = findViewById<TextView>(R.id.txt_major_state_in)    // 대주주요건 상단(해당 주식 없음) 이름설정
        user_name_major_in.text = getString(R.string.txt_major_state_in, uName,major_count) // 몇개 대주주인지 알려주는 TextView
        user_name_info.text = getString(R.string.txt_intro, uName)



        val intro_txt = SpannableStringBuilder(user_name_info.text)
        intro_txt.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            uName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        ) // 이름 크기 ↑
        intro_txt.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            uName.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        ) // Style : Bold
        user_name_info.text = intro_txt


        edit_expect_income = findViewById(R.id.edit_expect_income)
        btn_delete_exp = findViewById(R.id.btn_delete_exp)
        btn_delete_exp.setOnClickListener{
            edit_expect_income.setText("0")
        }

    }

    // 1. 대주주 해당
    private fun setMajorRecycler(){
        majorAdapter = MajorAdapter(this, arrMajor)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerview = findViewById<RecyclerView>(R.id.major_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = majorAdapter
//        recyclerview.isNestedScrollingEnabled = false
        majorAdapter.notifyDataSetChanged() // 갱신
    }
    // 2. 대주주 요건 충족
    private fun setMajorForeRecycler(){
        majorForeAdapter = MajorAdapter(this, arrMajorFore)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerview = findViewById<RecyclerView>(R.id.major_semi_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = majorForeAdapter

        majorForeAdapter.notifyDataSetChanged() // 갱신
    }

    // 3. 예상 금융소득
    private fun setExpectRecycler(){
        expectAdapter = ExpectAdapter(this, arrData)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerview = findViewById<RecyclerView>(R.id.expect_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = expectAdapter
//        recyclerview.isNestedScrollingEnabled = false
        expectAdapter.notifyDataSetChanged() // 갱신

        val expect_income = findViewById<TextView>(R.id.expect_income)
        var expect_sum = 0

        // 예상소득 TextView 값 세팅
        arrData.forEach {
            expect_sum += it.product_price.toInt()
        }
        expect_income.text =  dataFormat.format(expect_sum).toString()
    }

    // 4. 금융소득 종합과세 계산
    private fun setTaxRecycler(){
        // 소득 종합 데이터 비우기
        arrTax.clear()
        // 예금은 빼고 데이터 구성
        for (arrDatum in arrData) {
            if(arrDatum.product_type != "2"){
                arrTax.add(arrDatum)
            }
        }

        taxAdapter = TaxAdapter(this, arrTax)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerview = findViewById<RecyclerView>(R.id.tax_recyclerview)
        recyclerview.layoutManager = linearLayoutManager
        // adapter
        recyclerview.adapter = taxAdapter
//        recyclerview.isNestedScrollingEnabled = false

        taxAdapter.notifyDataSetChanged() // 갱신
    }


    private fun setHeaderView(){
        main_scroll_view = findViewById<NewScrollView>(R.id.main_scroll_view)
        tab_ll = findViewById<TabLayout>(R.id.header_view)
        (main_scroll_view as NewScrollView?)?.setHeader(tab_ll)
    }

    private fun setData(){
        /** 보유 자산 데이터 세팅**/
        arrData.add(ExpectData("삼성전자", "1", "78500", "1", "2020.12.03", "1400", "1.58", "1000000"))
        arrData.add(ExpectData("SK", "1", "750000", "1", "2020.12.03", "500", "0.6", "1000000"))
        arrData.add(
            ExpectData(
                "KODEX S&P",
                "3",
                "78500",
                "3",
                "2020.12.03",
                "2500",
                "2.25",
                "1000000"
            )
        )
        arrData.add(
            ExpectData(
                "지급식증권투자신탁(주식형) 펀드",
                "3",
                "750000",
                "3",
                "2020.12.03",
                "591",
                "2.80",
                "1000000"
            )
        )
        arrData.add(ExpectData("저축예금 결산이자", "2", "78500", "2", "2020.12.03", "0", "0", "1000000"))
        arrData.add(
            ExpectData(
                "현대차",
                "1",
                "138000",
                "1",
                "2020.12.03",
                "22000",
                "10.45",
                "1000000"
            )
        )

        /** 대주주 해당 데이터 세팅**/
        arrMajor.add(MajorData("삼성전자", "KOSDAC", "34416", "150000000", "2.45"))
        arrMajor.add(MajorData("SK하이닉스", "KOSDAC", "64894", "864500000", "5.12"))
        arrMajor.add(MajorData("BGF리테일", "KOSDAC", "4520", "130000", "2.15"))
        arrMajor.add(MajorData("네이버", "KOSDAC", "800", "130000", "2.87"))

        /** 대주주 요건 충족 데이터 세팅**/
        arrMajorFore.add(MajorData("하이닉스", "KONEX", "64894", "205000000", "0.98"))
        arrMajorFore.add(MajorData("기아차", "KONEX", "8796", "7500000", "2.33"))
        arrMajorFore.add(MajorData("CU", "KONEX", "4520", "15000000", "2.15"))
        arrMajorFore.add(MajorData("카카오", "KONEX", "800", "1100000000", "2.87"))
    }

    /**
     * 날짜 세팅 함수
     */
    fun setDate(){
        val now: Long = System.currentTimeMillis()
        val mDate = Date(now)
        val format_date_toTime = SimpleDateFormat("yyyy.MM.dd hh:mm")

        val date_toTime = format_date_toTime.format(mDate)
        val date_year = date_toTime.substring(0, 4).toInt() // 년도만 빼냄

        // 오늘 날짜 세팅
        today_tv = findViewById(R.id.txt_today)
        today_tv.text = date_toTime

        // 대주주 해당 기준 날짜
        txt_major_standard_date = findViewById<TextView>(R.id.txt_major_standard_date)
        txt_major_standard_date.text = getString(R.string.txt_major_standard_date,date_year)

        // 대주주 요건 충족 기준 날짜
        txt_major_fore_date = findViewById<TextView>(R.id.txt_major_fore_date)
        txt_major_fore_date.text = getString(R.string.txt_major_fore_date,(date_year-1))
    }

    /**
     * 뷰 확대 에니메이션 함수
     */
    fun expand(v: View) {
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) WindowManager.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
        v.startAnimation(a)
    }

    /**
     * 뷰 축소 에니메이션 함수
     */
    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        // Collapse speed of 1dp/ms
        a.duration = initialHeight / v.context.resources.displayMetrics.density.toLong()
        v.startAnimation(a)
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
                (parent.getChildAt(0) as TextView).setTextColor(Color.BLACK) // -> 깜빡거리면서 설정됨.. 다른 방법으로..
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
