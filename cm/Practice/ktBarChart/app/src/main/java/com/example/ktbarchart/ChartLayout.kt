package com.example.ktbarchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ktbarchart.control.BarItem

class ChartLayout : RelativeLayout {

    var chartHeight : Int = 0
    var chartWidth : Int = 0

    var xAxis : String = ""
    var yAxis : String = ""

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
        chartHeight = height - 20
        chartWidth = width - 20

        viewMain = LayoutInflater.from(context).inflate(R.layout.chart_layout,this)
        chartLayout = viewMain.findViewById(R.id.chartLayout)
        axis_bottom = viewMain.findViewById(R.id.axisBottom)
        axis_right = viewMain.findViewById(R.id.axisRight)

        txt_bottom = axis_bottom.findViewById(R.id.txtBottom)
        txt_right = axis_right.findViewById(R.id.txtRight)

        txt_bottom.text = xAxis
        txt_right.text = yAxis


        chartHeight = chartLayout.layoutParams.height
        chartWidth = chartLayout.layoutParams.width

        Log.d("Chart_Size","onDraw ==> window_Height: $height, window_Width: $width, chart_Height: $chartHeight, chart_width: $chartWidth")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(changed){
            // 생성
            chartHeight = height - 20
            chartWidth = width - 20

            viewMain = LayoutInflater.from(context).inflate(R.layout.chart_layout,this)
            chartLayout = viewMain.findViewById(R.id.chartLayout)
            axis_bottom = viewMain.findViewById(R.id.axisBottom)
            axis_right = viewMain.findViewById(R.id.axisRight)

            txt_bottom = axis_bottom.findViewById(R.id.txtBottom)
            txt_right = axis_right.findViewById(R.id.txtRight)

            txt_bottom.text = xAxis
            txt_right.text = yAxis


            chartHeight = chartLayout.layoutParams.height
            chartWidth = chartLayout.layoutParams.width

            Log.d("Chart_Size","onLayout ==> window_Height: $height, window_Width: $width, chart_Height: $chartHeight, chart_width: $chartWidth")
        }
    }

    private fun init(){
        viewMain = LayoutInflater.from(context).inflate(R.layout.chart_layout,this)
        chartLayout = viewMain.findViewById(R.id.chartLayout)
        axis_bottom = viewMain.findViewById(R.id.axisBottom)
        axis_right = viewMain.findViewById(R.id.axisRight)
        txt_bottom = axis_bottom.findViewById(R.id.txtBottom)
        txt_right = axis_right.findViewById(R.id.txtRight)
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartHeight = height - 20
        chartWidth = width - 20

        viewMain = LayoutInflater.from(context).inflate(R.layout.chart_layout,this)
        chartLayout = viewMain.findViewById(R.id.chartLayout)
        axis_bottom = viewMain.findViewById(R.id.axisBottom)
        axis_right = viewMain.findViewById(R.id.axisRight)

        txt_bottom = axis_bottom.findViewById(R.id.txtBottom)
        txt_right = axis_right.findViewById(R.id.txtRight)

        txt_bottom.text = xAxis
        txt_right.text = yAxis


        chartHeight = chartLayout.layoutParams.height
        chartWidth = chartLayout.layoutParams.width

        Log.d("Chart_Size","onDraw ==> window_Height: $height, window_Width: $width, chart_Height: $chartHeight, chart_width: $chartWidth")
    }

    public fun setData(xValue : String, yValue : String){

        xAxis = xValue
        yAxis = yValue

        invalidate()
    }
}