package com.example.ktbarchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ktbarchart.data.ChartData
import com.example.ktbarchart.data.ChartDataSet
import com.example.ktbarchart.view.BongItem
import com.example.ktbarchart.view.ChartLayout

class MainActivity : AppCompatActivity() {
    lateinit var chartLayout : ChartLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        chartLayout = findViewById(R.id.chart)
        val data1 = ChartData(200,250,260,180)
        val data2 = ChartData(250,250,280,220)
        val data3 = ChartData(220,180,230,120)

        val chartDataSet = ChartDataSet(arrayListOf(data1,data2,data3))

        chartLayout.setData(chartDataSet)
    }
}