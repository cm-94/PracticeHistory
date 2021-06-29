package com.example.ktbarchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ktbarchart.control.BarItem

class ChartLayout : RelativeLayout {

    var chartHeight : Int = 0;
    var chartWidth : Int = 0;

    lateinit var viewMain : View

    lateinit var chartLayout : LinearLayout
    lateinit var axis_bottom : LinearLayout
    lateinit var axis_right : LinearLayout

    lateinit var txt_bottom : TextView
    lateinit var txt_right : TextView

    lateinit var chartData : ArrayList<BarItem>
    var barColor = Color.BLACK

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context != null){
            defaultAttrs(context,attrs)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(changed){
            // 생성

        }
    }

    private fun init(){
        chartHeight = height - 20
        chartWidth = width - 20
        viewMain = LayoutInflater.from(context).inflate(R.layout.chart_layout,this)
        chartLayout = viewMain.findViewById(R.id.chartLayout)
        axis_bottom = viewMain.findViewById(R.id.axisBottom)
        axis_right = viewMain.findViewById(R.id.axisRight)


        chartHeight = chartLayout.layoutParams.height
        chartWidth = chartLayout.layoutParams.height

        txt_bottom = axis_bottom.findViewById(R.id.txtBottom)
        txt_right = axis_right.findViewById(R.id.txtRight)
        txt_bottom.text = "bottom"
        txt_right.text = "right"
        invalidate()
    }

    var BG_BLUE :Int = 0
    var BG_RED :Int = 0
    var BG_TITLE_BLUE :Int = 0
    var BG_TITLE_RED :Int = 0
    var BG_SUM :Int = 0

    private fun defaultAttrs(context : Context,attrs : AttributeSet?) {
        BG_BLUE = ContextCompat.getColor(context, R.color.blue)
        BG_RED = ContextCompat.getColor(context, R.color.red)

//        height_bottom = context.resources.getDimensionPixelOffset(R.dimen.common_height_title)
        val a = context.obtainStyledAttributes(attrs, R.styleable.BarChart)

        barColor = a.getInt(R.styleable.BarChart_barColor, R.color.black)
        a.recycle()


        init()
    }
}