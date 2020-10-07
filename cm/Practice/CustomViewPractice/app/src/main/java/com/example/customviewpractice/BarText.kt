package com.example.customviewpractice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.util.jar.Attributes

class BarText(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    data class Bar(
        val nameView : View,
        val barView : View,
        val labelView : View,
        val value : Double
    )

    private val bars = mutableListOf<Bar>()

    private val barMarginH = 10
    private val barMarginV = 2

    fun add(bar: Bar) {
        bars.add(bar)

        addView(bar.nameView)
        addView(bar.barView)
        addView(bar.labelView)
    }
    fun clear(){
        bars.clear()
        removeAllViews()
    }

    /**
     * view의 크기를 결정!
     * setMeasuredDimension() => width, height 세팅
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val captionMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val labelMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        for (bar in bars) {
            bar.nameView.measure(captionMeasureSpec, captionMeasureSpec)
            bar.labelView.measure(labelMeasureSpec, labelMeasureSpec)
        }

        // 가장 높은 Height 기준으로 layout_height 지정
        val maxCaptionHeight = bars.map{it.nameView.measuredHeight}.max() ?: 0

        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            maxCaptionHeight * bars.size
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (bars.size == 0) {
            return
        }

        /** 1.itemHeight : item의 개별 높이 */
        val itemHeight = (bottom - top) / bars.size

        /** 2.itemHeight : item의 개별 높이 */
        val nameRight = bars.map{it.nameView.measuredWidth}.max() ?: 0
        val maxValue = bars.map{it.value}.max() ?: 0.0

        for ((index, bar) in bars.withIndex()) {
            val nameLeft = nameRight - bar.nameView.measuredWidth
            val nameTop = index * itemHeight
            val nameBottom = nameTop + itemHeight

            bar.nameView.layout(
                nameLeft,
                nameTop,
                nameRight,
                nameBottom
            )

            var barWidth = 0
            if (maxValue > 0.0) {
                barWidth = (bar.value * (right - nameRight - barMarginH * 2) / maxValue).toInt()
            }

            val barLeft = nameRight
            val barTop = index * itemHeight
            val barRight = barLeft + barWidth + barMarginH * 2
            val barBottom = barTop + itemHeight
            bar.barView.layout(
                barLeft + barMarginH,
                barTop + barMarginV,
                barRight - barMarginH,
                barBottom - barMarginV
            )

            val labelWidth = bar.labelView.measuredWidth
            val spaceLeftForLabel = barWidth - 2 * barMarginH

            val labelLeft =
                if (spaceLeftForLabel >= labelWidth) {
                    barRight - labelWidth - barMarginH
                } else {
                    barRight
                }

            val labelTop = barTop
            val labelRight = labelLeft + labelWidth
            val labelBottom = barBottom

            bar.labelView.layout(
                labelLeft,
                labelTop,
                labelRight,
                labelBottom
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }
//    var myData:Float = 0F
//    var maxData:Float = 0F
//    var count = 0
//
//    init {
//        inflate(context,0,this)
//
//        val attributes = context.obtainStyledAttributes(attrs,R.styleable.BarChart)
//        customText.text = attributes.getString(R.styleable.BarChart_text)
//
//        attributes.recycle()
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        // First, draw the bitmap as created.
//        val mPaint = Paint()
//        mPaint.color = Color.BLUE
////        mPaint.style = Paint.Style.FILL;
//        if (count%2 == 1){
//            customText.setBackgroundColor(Color.RED)
//        }else{
//            customText.setBackgroundColor(Color.BLUE)
//        }
//        count++
//
////        addView(customBar)
////        addView(customText)
////        canvas?.drawText("Test",1F,1F,mPaint)
//        customText.text = this.myData.toString()
//        customText.gravity = right
//        customText.setTextColor(Color.BLACK)
//        canvas?.drawRect(1F,2F,5F,5F,mPaint)
//    }
//
//    @SuppressLint("DrawAllocation")
//    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
//        var canvas = Canvas()
//        val mpaint = Paint()
//        mpaint.color = Color.BLUE
//        canvas.drawText("test", p1.toFloat(), p3.toFloat(),mpaint)
//
//
//    }
//
//
//    fun setDrawData(data:Float,maxData:Float){
//        this.myData = data
//        this.maxData = maxData
//        invalidate()
//    }
}