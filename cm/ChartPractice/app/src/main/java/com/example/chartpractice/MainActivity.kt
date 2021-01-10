package com.example.chartpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Part1 -> 차트에 들어갈 데이터 Array & View 초기화
        val entries = ArrayList<Entry>()
        val lineChart = findViewById<LineChart>(R.id.lineChart)

        //Part2 -> 각 데이터 추가
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

        //Part3 -> 차트 데이터로 세팅
        val vl = LineDataSet(entries, "My Type")

        //Part4 -> 차트 데이터 속성 지정
        vl.setDrawValues(false)      // 각 꼭지점마다 데이터 표시 여부
        vl.setDrawFilled(false)      // 그래프 하단 채우기 여부
        vl.lineWidth = 1f            // 라인 두께
        vl.fillColor = R.color.gray  // 하단 채우기 색
        vl.fillAlpha = R.color.red   // 하단 채우기 알파

        //Part5 - x축 텍스트 기울기, 시계방향 회전
//        lineChart.xAxis.labelRotationAngle = 5f

        //Part6 -> 차트데이터 세팅 ( lineChart.setData(LineData(vl)) )
        lineChart.data = LineData(vl)

        //Part7 ->
        lineChart.axisRight.isEnabled = true     // 우측 y축 기준 사용여부
        lineChart.axisLeft.isEnabled = true      // 좌측 y축 기준 사용여부
        lineChart.xAxis.axisMaximum = 5 + 0.2f         // x축 한칸 너비(데이터)

        //Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(false)

        //Part9
        lineChart.description.text = "Days"      // 우측 하단 데이터 설명 텍스트
        lineChart.setNoDataText("No forex yet!") // 데이터 없을 때 텍스트

        //Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
//        val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
//        lineChart.marker = markerView
    }
}