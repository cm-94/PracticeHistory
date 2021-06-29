package com.example

import com.example.ktbarchart.data.BarData

class ChartDataSet {
    var data : ArrayList<BarData> = arrayListOf()
    var minValue = 0
    var maxValue = 0

    constructor(newData: ArrayList<BarData>) {
        if(newData.size > 0){
            for(item in newData){
                if(item.low < minValue) minValue = item.low
                if(item.high < maxValue) maxValue = item.high
            }
        }
        this.data = newData
    }
}