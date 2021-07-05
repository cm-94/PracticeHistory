package com.example.chartpractice.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.ktbarchart.data.ChartData
import com.example.ktbarchart.data.ChartDataSet


class BongChartLayout : View{
    private var chartDataSet = ChartDataSet()

    private val paint = Paint()
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        Log.d("onDraw_BongChart","height: " + this.height + ", width: " + this.width + ", data: " + chartDataSet.data.size)
        super.onDraw(canvas)
        if (chartDataSet.data.size > 0){
            Log.d("onDraw_BongChart", "paddingTop: $paddingTop, paddingBottom: $paddingBottom, paddingRight: $paddingRight, paddingLeft: $paddingLeft")
            paint.strokeWidth = 2F
            paint.color = Color.BLACK
            paint.isAntiAlias = true
            paint.isDither = true

            //Draw the coordinates first
            //X axis
            /**
             * Draw X axis
             * The parameters are: start X-axis coordinate, Y-axis coordinate, end X-axis coordinate, Y-axis coordinate, pen
             * X axis: the Y axis coordinate value of the start and end remains unchanged
             */
            // 아래측 X축일 때
            canvas.drawLine(paddingLeft + 0F, height.toFloat() - paddingBottom, width.toFloat() - paddingRight, height.toFloat() - paddingBottom, paint)
            // 상단 X축일 때
//        canvas.drawLine(paddingLeft + 0F, paddingBottom + 0F, width.toFloat() - paddingRight,paddingBottom + 0F, paint)

            /**
             * In the same way, draw the Y axis
             * Y-axis and X-axis coordinate values ​​remain unchanged
             */
            // 우측 Y축일 때
            canvas.drawLine(width.toFloat() - paddingRight, height.toFloat() - paddingBottom, width.toFloat() - paddingRight, paddingTop + 0F, paint)
            // 좌측 Y축일 때
//        canvas.drawLine(paddingLeft + 0F, height.toFloat() - paddingBottom, paddingLeft - 0F, paddingTop + 0F, paint)

            // 차트 영역 잡기
            val chartWidth = width/* - paddingLeft - paddingRight*/
            val chartHeight = height/* - paddingTop - paddingBottom*/

            // 한 봉의 영역 너비 잡기
            val itemWidth = chartWidth / chartDataSet.data.size

            // 한 봉의 사각형 너비 잡기(98 / 100)
            val bongWidth = (itemWidth) - (2 * (itemWidth / 100))

            // 한 봉의 영역 높이 잡기
            val dataHeight = chartDataSet.maxValue - chartDataSet.minValue

            // 한 봉의 라인 높이 잡기

            // 시가 & 종가(사각형) 시작점 X,Y (왼쪽부터 그릴 때)
            var startBongX = 0F /*+ paddingLeft */+ (itemWidth - bongWidth) / 2 // => 봉 너비 절반만큼 좌우 패딩을 줄거라..
            var startBongY = 0F
            var endBongX = 0F
            var endBongY = 0F

            // 시가 & 종가(사각형) 시작점 X,Y (왼쪽부터 그릴 때)
            var startLineX = 0F + paddingLeft + (itemWidth) / 2 // => 봉 너비 만큼 추가해야 봉 딱 가운데로 감!!
            var startLineY = 0F
            var endLineX = 0F
            var endLineY = 0F

            for (i in 0 until chartDataSet.data.size) {
                val data = chartDataSet.data[i]
                // 1. 색깔 지정
                paint.color = setColor(data)

                /** 봉 그리기 */
                // 2.1 좌표
                //  높이 재기
                //  음봉
                if(data.open > data.close){
                    startBongY = (paddingTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight).toFloat()
                    endBongY = (paddingTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight).toFloat()
                }
                //  양봉
                else if(data.open < data.close){
                    startBongY = (paddingTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight).toFloat()
                    endBongY = (paddingTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight).toFloat()
                }
                //  횡보?
                else{
                    startBongY = (paddingTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight).toFloat()
                    endBongY = (paddingTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight + 10).toFloat() // 최소 두께 10으로 임의 세팅
                }
                val rectF = RectF(startBongX, startBongY, startBongX + bongWidth, endBongY)
                // 2.2 그리기
                canvas.drawRect(rectF, paint)
                // 2.3 x 시작점 다음으로 이동
                startBongX += itemWidth
                endBongX += itemWidth

                /** 라인 그리기 */
                // 3.1 좌표
                startLineY = (paddingTop + (chartDataSet.maxValue - data.high) * chartHeight / dataHeight).toFloat()
                endLineY = (paddingTop + (chartDataSet.maxValue - data.low) * chartHeight / dataHeight).toFloat()
                // 3.2 그리기
                canvas.drawLine(startLineX,startLineY,startLineX,endLineY, paint)
                // 3.3 x 시작점 다음으로 이동
                startLineX += itemWidth
            }
        }
    }

    private fun init(){
        setWillNotDraw(false)
        chartLayout = LinearLayout(context)
        chartLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        chartLayout.orientation = LinearLayout.HORIZONTAL
        val p = chartLayout.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(0, 0, 45, 45)
    }

    // 차트 데이터 세팅
    fun setData(data: ChartDataSet) {
        chartDataSet.clear()
        chartDataSet = data
        invalidate() // onDraw 다시 호출
    }

    fun setColor(data : ChartData) : Int {
        if (data.open > data.close) return Color.BLUE
        else if (data.open < data.close) return Color.RED
        else return Color.BLACK
    }

    // 봉 마진 세팅 함수
    // 상하 크기를 잡기 위해 사용
    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }
}