package com.example.ktbarchart.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ktbarchart.R
import com.example.ktbarchart.data.ChartData
import com.example.ktbarchart.data.ChartDataSet


class ChartLayout : View {

    private val paint = Paint()
    lateinit private var chartDataSet: ChartDataSet

    var paddingInsideLeft = 0F
    var paddingInsideRight = 0F
    var paddingInsideBottom = 0F
    var paddingInsideTop = 0F

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context != null && attrs != null){
            setWillNotDraw(false)
        }
    }

    constructor(context: Context?) : super(context){
        if(context != null){
            setWillNotDraw(false)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (chartDataSet.data.size > 0){
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

            /**
             * Draw a cylinder
             * It's actually a rectangle
             * Width = right-left
             * Height = bottom-top
             * The width is fixed at 50
             */
            /**
             * Draw a cylinder
             * It's actually a rectangle
             * Width = right-left
             * Height = bottom-top
             * The width is fixed at 50
             */


            // 차트 영역 잡기
            val chartWidth = width - paddingLeft - paddingRight - paddingInsideLeft - paddingInsideRight
            val chartHeight = height - paddingTop - paddingBottom - paddingInsideTop - paddingInsideBottom

            // 한 봉의 영역 너비 잡기

            val itemWidth = chartWidth / chartDataSet.data.size

            // 한 봉의 사각형 너비 잡기(영역 / 2)
            val bongWidth = itemWidth / 2

            // 한 봉의 영역 높이 잡기
            val dataHeight = chartDataSet.maxValue - chartDataSet.minValue

            // 한 봉의 라인 높이 잡기

            // 시가 & 종가(사각형) 시작점 X,Y (왼쪽부터 그릴 때)
            var startBongX = 0 + paddingLeft + paddingInsideLeft + (bongWidth / 2) // => 봉 너비 절반만큼 좌우 패딩을 줄거라..
            var startBongY = 0F
            var endBongX = 0F
            var endBongY = 0F

            // 시가 & 종가(사각형) 시작점 X,Y (왼쪽부터 그릴 때)
            var startLineX = 0 + paddingLeft + paddingInsideLeft + (bongWidth) // => 봉 너비 만큼 추가해야 봉 딱 가운데로 감!!
            var startLineY = 0F
            var endLineX = 0F
            var endLineY = 0F

            for (i in 0 until chartDataSet.data.size) {
                var data = chartDataSet.data[i]
                // 1. 색깔 지정
                paint.setColor(setColor(data))
                // 2. 좌표
                // 높이 재기
                // 음봉
                if(data.open > data.close){
                    startBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
                    endBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                }
                // 양봉
                else if(data.open < data.close){
                    startBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                    endBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight
                }
                // 횡보?
                else{
                    startBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.close) * chartHeight / dataHeight
                    endBongY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.open) * chartHeight / dataHeight + 10 // 최소 두께 10으로 임의 세팅
                }
                val rectF = RectF(startBongX, startBongY, startBongX + bongWidth, endBongY)
                // 3. 그리기
                canvas.drawRect(rectF, paint)
                // 4. x 시작점 이동
                startBongX += itemWidth
                endBongX += itemWidth

                // 2. 좌표
                // 음봉
                startLineY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.high) * chartHeight / dataHeight
                endLineY = paddingTop + paddingInsideTop + (chartDataSet.maxValue - data.low) * chartHeight / dataHeight
                // 3. 그리기
                canvas.drawLine(startLineX,startLineY,startLineX,endLineY, paint)
                // 4. x 시작점 이동
                startLineX += itemWidth
            }
        }
        Log.d("Chart_Size","onDraw ==> window_Height: $height, window_Width: $width")

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

    }
    fun setData(data: ChartDataSet) {
        chartDataSet = data
        postInvalidate()
    }

    fun setAxis(xAxis: String, yAxis : String) {

    }


    fun setColor(data : ChartData) : Int {
        if(data.open > data.close) return Color.BLUE
        else if(data.open < data.close) return Color.RED
        else return Color.BLACK
    }
}