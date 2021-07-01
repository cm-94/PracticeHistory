package com.example.chartpractice.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.chartpractice.R
import com.example.ktbarchart.data.ChartDataSet
import com.example.ktbarchart.view.BongItem


class BongChartLayout : LinearLayout {
    private val paint = Paint()
    lateinit private var chartDataSet: ChartDataSet

    var paddingInsideLeft = 0F
    var paddingInsideRight = 0F
    var paddingInsideBottom = 0F
    var paddingInsideTop = 0F

    lateinit var chartLayout : LinearLayout

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context != null && attrs != null){
//            setWillNotDraw(false)
            init()
        }
    }

    constructor(context: Context?) : super(context){
        if(context != null){
//            setWillNotDraw(false)
            init()
        }
    }

//    @SuppressLint("DrawAllocation")
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        Log.d("Test_ChartLayout_onDraw","height: " + this.height + ", width: " + this.width + ", context: " + context)
//        if (chartDataSet.data.size > 0){
//            paint.strokeWidth = 2F
//            paint.color = Color.GRAY
//            paint.isAntiAlias = true
//            paint.isDither = true
//
//            /** draw the X axis */
//            // 아래측 X축일 때
//            canvas.drawLine(paddingLeft + 0F, height.toFloat() - paddingBottom, width.toFloat() - paddingRight, height.toFloat() - paddingBottom, paint)
//            // 상단 X축일 때
////            canvas.drawLine(paddingLeft + 0F, paddingBottom + 0F, width.toFloat() - paddingRight,paddingBottom + 0F, paint)
//
//            /** draw the Y axis */
//            // 우측 Y축일 때
//            canvas.drawLine(width.toFloat() - paddingRight, height.toFloat() - paddingBottom, width.toFloat() - paddingRight, paddingTop + 0F, paint)
//            // 좌측 Y축일 때
////            canvas.drawLine(paddingLeft + 0F, height.toFloat() - paddingBottom, paddingLeft - 0F, paddingTop + 0F, paint)
//
//
//            // 차트 영역 잡기
//            val chartWidth = width - paddingLeft - paddingRight - paddingInsideLeft - paddingInsideRight
//            val chartHeight = height - paddingTop - paddingBottom - paddingInsideTop - paddingInsideBottom
//            // 한 봉의 영역 너비 잡기
//            val dataWidth = chartWidth / chartDataSet.data.size
//
//            // 한 봉의 영역 높이 잡기
//            val dataHeight = chartDataSet.maxValue - chartDataSet.minValue
//
//
//            // 한 봉의 사각형 너비 잡기(영역 / 2)
//            val bongWidth = dataWidth / 2
//
//            var posTop = 0F
//            var posBottom = 0F
//            // TODO : onDraw가 계속 호출돼서 중복으로 그림..
//            //   해결 방법 찾으면 삭제 필요..
////            chartLayout.removeAllViews()
//
//            for (i in 0 until chartDataSet.data.size) {
//                var data = chartDataSet.data[i]
//                if(data.open > data.close){
//                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
//                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
//                }
//                // 양봉
//                else if(data.open < data.close){
//                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
//                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
//                }
//                // 횡보?
//                else{
//                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
//                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight + 10 // 최소 두께 10으로 임의 세팅
//                }
//
//                val bongItem = BongItem(context)
//                bongItem.layoutParams = LinearLayout.LayoutParams(
//                    dataWidth.toInt(),
//                    LinearLayout.LayoutParams.MATCH_PARENT
//                )
////                bongItem.layoutParams.
////                bongItem.marginBottom =
//                bongItem.setData(chartDataSet.data[i])
//
//                chartLayout.addView(bongItem)
//            }
//        }
//    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(changed){
            // 차트 영역 잡기
            val chartWidth = width - paddingLeft - paddingRight - paddingInsideLeft - paddingInsideRight
            val chartHeight = height - paddingTop - paddingBottom - paddingInsideTop - paddingInsideBottom
            // 한 봉의 영역 너비 잡기
            val dataWidth = chartWidth / chartDataSet.data.size

            // 한 봉의 영역 높이 잡기
            val dataHeight = chartDataSet.maxValue - chartDataSet.minValue


            // 한 봉의 사각형 너비 잡기(영역 / 2)
            val bongWidth = dataWidth / 2

            var posTop = 0F
            var posBottom = 0F
            // TODO : onDraw가 계속 호출돼서 중복으로 그림..
            //   해결 방법 찾으면 삭제 필요..
//            chartLayout.removeAllViews()

            for (i in 0 until chartDataSet.data.size) {
                val data = chartDataSet.data[i]
                if(data.open > data.close){
                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                }
                // 양봉
                else if(data.open < data.close){
                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
                }
                // 횡보?
                else{
                    posTop = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                    posBottom = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight + 10 // 최소 두께 10으로 임의 세팅
                }

                val bongItem = BongItem(context)

                bongItem.layoutParams = LinearLayout.LayoutParams(
                        dataWidth.toInt(),
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
//                bongItem.layoutParams.
//                bongItem.marginBottom =
                bongItem.setData(chartDataSet.data[i])

                var bongMarginTop = 0
                var bongMarginBottom = 0
                // 위아래 높이 조정 -> Margin으로!!
                bongMarginTop = (paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.high) * chartHeight / dataHeight).toInt()
                bongMarginBottom = height - (paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.low) * chartHeight / dataHeight).toInt()

                setMargins(bongItem,0,bongMarginTop,0,bongMarginBottom)
                chartLayout.addView(bongItem)
            }
        }
    }

    private fun init(){
        val v: View = LayoutInflater.from(context).inflate(R.layout.chart_layout, this, true)

        chartLayout = v.findViewById(R.id.chartLayout)
    }

    fun setData(data: ChartDataSet) {
        chartDataSet = data
        postInvalidate()
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }
}