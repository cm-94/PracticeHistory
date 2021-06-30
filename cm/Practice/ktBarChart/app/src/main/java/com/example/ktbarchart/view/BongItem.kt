package com.example.ktbarchart.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.ktbarchart.data.ChartData


class BongItem : View {
    lateinit var itemData : ChartData
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.rgb(55, 55, 55))

        val redPaint = Paint()
        redPaint.color = Color.rgb(255, 0, 0)

        val whitePaint = Paint()
        whitePaint.color = Color.WHITE

        val bluePaint = Paint()
        bluePaint.color = Color.rgb(0, 0, 255)

        canvas!!.drawCircle(700F, 500F, 200F, redPaint)

        canvas!!.drawLine(0F, 0F, 800F, 800F, whitePaint)
        canvas!!.drawCircle(325F, 600F, 300F, bluePaint)

    }

    fun setData(data : ChartData){
        this.itemData =  data
    }
}