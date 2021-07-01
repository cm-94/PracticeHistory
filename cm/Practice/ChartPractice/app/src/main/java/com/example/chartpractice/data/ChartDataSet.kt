package com.example.ktbarchart.data

class ChartDataSet {
    var data : ArrayList<ChartData> = arrayListOf()
    var minValue = Int.MAX_VALUE
    var maxValue = Int.MIN_VALUE

    constructor(newData: ArrayList<ChartData>) {
        if(newData.size > 0){
            for(item in newData){
                if(item.low < minValue) minValue = item.low
                if(item.high > maxValue) maxValue = item.high
            }
        }
        this.data = newData
    }
}