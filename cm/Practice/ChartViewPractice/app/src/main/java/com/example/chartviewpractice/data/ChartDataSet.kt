package com.example.ktbarchart.data

import kotlin.math.min

class ChartDataSet {
    var data : ArrayList<ChartData> = arrayListOf()
    var minValue = Int.MAX_VALUE
    var maxValue = Int.MIN_VALUE
    constructor()
    constructor(newData: ArrayList<ChartData>) {
        if(newData.size > 0){
            for(item in newData){
                if(item.low < minValue) minValue = item.low
                if(item.high > maxValue) maxValue = item.high
            }
        }
        this.data = newData
    }

    fun clear(){
        this.data.clear()
        this.minValue = Int.MAX_VALUE
        this.maxValue = Int.MIN_VALUE
    }
}