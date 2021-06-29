package com.example.ktbarchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    lateinit var chartLayout : ChartLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chartLayout = findViewById(R.id.chart)
        chartLayout.setData("x축","y축")

    }
}