package com.example.ktbarchart.view

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
import com.example.ktbarchart.data.ChartDataSet


class ChartLayout : View {

    var chartHeight : Int = 0
    var chartWidth : Int = 0

    private val paint = Paint()
    lateinit private var chartDataSet: ChartDataSet

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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

        //Draw the coordinates first
        //X axis
        /**
         * Draw X axis
         * The parameters are: start X-axis coordinate, Y-axis coordinate, end X-axis coordinate, Y-axis coordinate, pen
         * X axis: the Y axis coordinate value of the start and end remains unchanged
         */
        canvas.drawLine(50F, height.toFloat() - 50F, width.toFloat() - 50F, height.toFloat() - 50F, paint)
        /**
         * The small arrow on the right of the X axis
         * Here you need to find the X and Y coordinate values ​​at the end of the X axis, which is the code above (850, 550)
         * Then draw two diagonal lines with this coordinate as the starting point
         * The X axis is completed here
         */
        /**
         * The small arrow on the right of the X axis
         * Here you need to find the X and Y coordinate values ​​at the end of the X axis, which is the code above (850, 550)
         * Then draw two diagonal lines with this coordinate as the starting point
         * The X axis is completed here
         */
//        canvas.drawLine(850F, 550F, 840F, 540F, paint)
//        canvas.drawLine(850F, 550F, 840F, 560F, paint)

        /**
         * In the same way, draw the Y axis
         * Y-axis and X-axis coordinate values ​​remain unchanged
         */
        /**
         * In the same way, draw the Y axis
         * Y-axis and X-axis coordinate values ​​remain unchanged
         */
        canvas.drawLine(width.toFloat() - 50F, height.toFloat() - 50F, width.toFloat() - 50F, 50F, paint)
//        canvas.drawLine(50F, 50F, 40F, 60F, paint)
//        canvas.drawLine(50F, 50F, 60F, 60F, paint)

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
        paint.setColor(Color.RED)
        paint.setTextSize(30F)
        if (chartDataSet.data.size > 0) for (i in 0 until chartDataSet.data.size) {
            val rectF = RectF((150 + 100 * i).toFloat(), (width.toFloat() - chartDataSet.data[i].close).toFloat(), (100 + 100 * i).toFloat(), width.toFloat())
            canvas.drawRect(rectF, paint)
        }
        Log.d("Chart_Size","onDraw ==> window_Height: $height, window_Width: $width, chart_Height: $chartHeight, chart_width: $chartWidth")
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
}