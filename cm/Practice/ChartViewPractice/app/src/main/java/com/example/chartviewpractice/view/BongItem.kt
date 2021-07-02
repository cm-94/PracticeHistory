package com.example.ktbarchart.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ktbarchart.data.ChartData


class BongItem : View {
    private val paint = Paint()
    lateinit var itemData : ChartData
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 2F
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.isDither = true

        // 한 봉의 사각형 너비 잡기(영역 / 2)
        // 시가 & 종가(사각형) 시작점 X,Y (왼쪽부터 그릴 때)
        // TODO : 임의로 한 봉의 너비의 100분의 1만큼 죄우 여백을 준다
        val bongWidth = width - (width / 100) * 2 // 좌우 1/100 만큼 여백 둠
        var startBongX = (width / 100).toFloat()
        var startBongY = 0F
        var endBongX = startBongX + bongWidth
        var endBongY = 0F

        // 라인 시작점 X,Y (왼쪽부터 그릴 때)
        var startLineX = (width / 2).toFloat() // => 전체 너비 반 만큼 추가해야 봉 딱 가운데로 감!!
        var startLineY = 0F
        var endLineX = 0F
        var endLineY = 0F
        // 1. 색깔 지정
        paint.color = setColor(itemData)
        // 2. 좌표
        // 높이 재기
        // 음봉
        if(itemData.open > itemData.close){
            startBongY = (height * (itemData.high - itemData.close) / (itemData.high - itemData.low)).toFloat()
            endBongY = (height * (itemData.high - itemData.open) / (itemData.high - itemData.low)).toFloat()
        }
        // 양봉
        else if(itemData.open < itemData.close){
            startBongY = (height * (itemData.high - itemData.open) / (itemData.high - itemData.low)).toFloat()
            endBongY = (height * (itemData.high - itemData.close) / (itemData.high - itemData.low)).toFloat()
        }
        // 횡보?
        else{
            startBongY = itemData.open.toFloat()
            endBongY = itemData.open.toFloat() + 5 // 최소 두께 10으로 임의 세팅
        }
        val rectF = RectF(startBongX, startBongY, startBongX + bongWidth, endBongY)
        // 3. 그리기
        canvas.drawRect(rectF, paint)


        Log.d("onDraw_BongItem","height: " + this.height + ", width: " + this.width + ", high: " + itemData.high +", low: " + itemData.low)
        this.height
        // 2. 좌표
        // 라인
//        startLineY = itemData.high.toFloat()
//        endLineY = itemData.low.toFloat()
        startLineY = 0F
        endLineY = height.toFloat()
        // 3. 그리기
        canvas.drawLine(startLineX,startLineY,startLineX,endLineY, paint)
    }

    fun setData(data : ChartData){
        this.itemData =  data
    }

    fun setColor(data : ChartData) : Int {
        if (data.open > data.close) return Color.BLUE
        else if (data.open < data.close) return Color.RED
        else return Color.BLACK
    }
}