package com.example.chartpractice.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.chartviewpractice.R
import com.example.ktbarchart.data.ChartDataSet

class ChartLayout : RelativeLayout {
    /** data **/
    private lateinit var dataSet: ChartDataSet
    private lateinit var chartLayout : BongChartLayout
    private lateinit var xAxis : TextView
    private lateinit var yAxis : TextView

    constructor(context: Context?) : super(context){
        if (context != null){
            init()
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if (context != null){
            init()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(changed){
            chartLayout.setData(dataSet)
        }
    }

    fun init() {
        val v: View = LayoutInflater.from(context).inflate(R.layout.chart_layout, this, true)
        chartLayout = v.findViewById(R.id.bongChartLayout)
    }

    fun setData(chartDataSet: ChartDataSet){
        dataSet = chartDataSet
//        invalidate()
    }


    fun setXAxisTitle(title: String){
        xAxis.text = title
    }
    fun setYAxisTitle(title: String){
        yAxis.text = title
    }
}