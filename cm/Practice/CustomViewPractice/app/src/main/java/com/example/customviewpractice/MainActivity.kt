package com.example.customviewpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mExchangeList : ArrayList<ExchangeRate> = arrayListOf()
    private lateinit var mExchangeAdapter: ExchangeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 입력 버튼
        inputButton.setOnClickListener{
            if(inputText!=null){
                myTextView.inputText(inputText.text.toString())
            }
        }


        // Spinner

        // TODO 1. spinner에서 표현할 데이터(mExchangeList : ArrayList<ExchangeRate>) 초기화
        initList()

//        // TODO 2. Spinner - adpater 초기화
//        mExchangeAdapter = ExchangeAdapter(this, mExchangeList)
//
//        // TODO 3. Spinner.adpater 초기화
//        var spinner : Spinner = findViewById(R.id.spinner)
//        spinner.adapter = mExchangeAdapter

        // TODO 4. Spinner.onItemSelectedListener 정의
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"아무것도 클릭 안됨",Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var clickedItem : ExchangeRate = parent?.getItemAtPosition(position) as ExchangeRate
                var exchangeRate :String = clickedItem.exchange_text.toString()
                Toast.makeText(applicationContext,exchangeRate+" 클릭됨",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun initList(){
        mExchangeList.add(ExchangeRate("한화",R.drawable.won))
        mExchangeList.add(ExchangeRate("달러",R.drawable.dollar))
        mExchangeList.add(ExchangeRate("엔",R.drawable.yen))

        mExchangeAdapter = ExchangeAdapter(this, mExchangeList)

        // TODO 3. Spinner.adpater 초기화
        var spinner : Spinner = findViewById(R.id.spinner)
        spinner.adapter = mExchangeAdapter
    }
}

