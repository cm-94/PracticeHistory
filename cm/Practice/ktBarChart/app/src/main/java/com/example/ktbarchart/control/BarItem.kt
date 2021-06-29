package com.example.ktbarchart.control

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.example.ktbarchart.data.BarData

class BarItem : RelativeLayout{
    lateinit var data : BarData
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,attrs,defStyleAttr)
}