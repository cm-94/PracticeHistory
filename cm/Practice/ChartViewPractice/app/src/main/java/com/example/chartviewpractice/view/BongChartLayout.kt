package com.example.chartpractice.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.chartviewpractice.R
import com.example.ktbarchart.data.ChartDataSet
import com.example.ktbarchart.view.BongItem


class BongChartLayout : LinearLayout {
    private val paint = Paint()
    private var chartDataSet = ChartDataSet()

    var paddingInsideLeft = 0F
    var paddingInsideRight = 0F
    var paddingInsideBottom = 0F
    var paddingInsideTop = 0F

    lateinit var chartLayout : LinearLayout

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context != null && attrs != null){
            init()
        }
    }

    constructor(context: Context?) : super(context){
        if(context != null){
            init()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        Log.d("onDraw_BongChart","height: " + this.height + ", width: " + this.width + ", data: " + chartDataSet.data.size)
        super.onDraw(canvas)
        // TODO : onDraw를 여러번 호출해서 자꾸 childView가 쌓임..
        //  BongItem 그릴 때 너비가 int라 약간 남은 공간에 또 그림
        //  일단 매번 자식뷰 지우도록..
        chartLayout.removeAllViews()
        // 차트 영역 크기
        val chartWidth = width - paddingLeft - paddingRight - paddingInsideLeft - paddingInsideRight
        val chartHeight = height - paddingTop - paddingBottom - paddingInsideTop - paddingInsideBottom
        // 한 봉의 영역 너비 잡기
        val dataWidth = chartWidth / chartDataSet.data.size

        // 한 봉의 영역 높이 잡기
        val dataHeight = chartDataSet.maxValue - chartDataSet.minValue

        for (i in 0 until chartDataSet.data.size) {
            val data = chartDataSet.data[i]

            val bongItem = BongItem(context)

            bongItem.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                1f
            )
            bongItem.setData(chartDataSet.data[i])

            var bongMarginTop = 0
            var bongMarginBottom = 0
            // 위아래 높이 조정 -> Margin으로!!
            bongMarginTop = (paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.high) * chartHeight / dataHeight).toInt()
            bongMarginBottom = (chartHeight - (paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.low) * chartHeight / dataHeight)).toInt()

            setMargins(bongItem,0,bongMarginTop,0,bongMarginBottom)
            chartLayout.addView(bongItem)
        }
    }

    private fun init(){
        setWillNotDraw(false)
        val v: View = LayoutInflater.from(context).inflate(R.layout.bong_chart_layout, this, true)
        chartLayout = v.findViewById(R.id.bongChart)
    }

    // 차트 데이터 세팅
    fun setData(data: ChartDataSet) {
        chartDataSet.clear()
        chartDataSet = data
//        invalidate() // onDraw 다시 호출
    }

    // 봉 마진 세팅 함수
    // 상하 크기를 잡기 위해 사용
    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }
}