package com.example.customviewpractice

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.github.mikephil.charting.charts.BarChart
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.barchart_view.view.*

class MainActivity : AppCompatActivity() {
    private var mExchangeList : ArrayList<ExchangeRate> = arrayListOf()
    private lateinit var mExchangeAdapter: ExchangeAdapter



    data class Item(
        val name: String,
        var value : Double
    )

    val items = mutableListOf(
        Item("April", 20.0),
        Item("May", 25.0),
        Item("June", 18.0),
        Item("July", 1.0),
        Item("August", 0.0),
        Item("Data", 0.0)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // BarText View Class 동적 생성
        var barchart = BarText(this)
        // mBarText(LinearLayout)에 addView
        findViewById<ViewGroup>(R.id.mBarText).addView(barchart)

        // 입력 버튼
        inputButton.setOnClickListener{
            if(inputText.text.toString()!=""){
                items[5].value = inputText.text.toString().toDouble()
                myTextView.inputText(inputText.text.toString())
                // 차트 비우기
                barchart.clear()
                // 차트 초기화
                initChart(barchart)
//                myBarChart.setDrawData(inputText.text.toString().toFloat(),13F)
            }
        }

//        myBarChart.setOnClickListener {
//            Toast.makeText(this,"my:${myBarChart.myData},max:${myBarChart.maxData},count:${myBarChart.count}",Toast.LENGTH_SHORT).show()
//        }
        // Spinner
        initSpinner()
    }

    fun initChart(barchart: BarText){
        for (item in items) {
            val nameView = TextView(this)
            nameView.text = item.name
//            nameView.setTypeface(nameView.typeface, Typeface.BOLD)
//            nameView.setPadding(defaultPaddingH, defaultPaddingV, defaultPaddingH, defaultPaddingV)

            val barView = View(this)
            barView.setBackgroundColor(Color.rgb(116, 197, 237))
//            barView.setPadding(defaultPaddingH, defaultPaddingV, defaultPaddingH, defaultPaddingV)
            barView.setOnClickListener{
                Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show()
            }

            val labelView = TextView(this)
            labelView.text = item.value.toString()
//            labelView.setTypeface(nameView.typeface, Typeface.BOLD)
//            labelView.setPadding(defaultPaddingH, defaultPaddingV, defaultPaddingH, defaultPaddingV)


            barchart.add(BarText.Bar(nameView, barView, labelView, item.value))
        }
    }


    fun initSpinner(){
        // TODO 1. spinner에서 표현할 데이터(mExchangeList : ArrayList<ExchangeRate>) 초기화
        mExchangeList.add(ExchangeRate("한화",R.drawable.won))
        mExchangeList.add(ExchangeRate("달러",R.drawable.dollar))
        mExchangeList.add(ExchangeRate("엔",R.drawable.yen))

        // TODO 2. Spinner - adpater 초기화
        mExchangeAdapter = ExchangeAdapter(this, mExchangeList)

        // TODO 3. Spinner.adpater 초기화
        val spinner : Spinner = findViewById(R.id.spinner)
        spinner.adapter = mExchangeAdapter

        // TODO 4. Spinner.onItemSelectedListener 정의
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"아무것도 클릭 안됨",Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var clickedItem : ExchangeRate = parent?.getItemAtPosition(position) as ExchangeRate
                var exchangeRate :String = clickedItem.exchange_text
                Toast.makeText(applicationContext,exchangeRate+" 클릭됨",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

