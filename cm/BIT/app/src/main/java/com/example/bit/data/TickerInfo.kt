package com.example.bit.data

class TickerInfo() {
    var opening_price:String = ""
    var closing_price:String = ""
    var min_price:String = ""
    var max_price:String = ""
    var units_traded:String = ""
    var acc_trade_value:String = ""
    var prev_closing_price:String = ""
    var units_traded_24H:String = ""
    var acc_trade_value_24H:String = ""
    var fluctate_24H:String = ""
    var fluctate_rate_24H:String = ""
    var date:String = ""


    // TODO : TickerData & TickerInfo의 보조생성자 필요성 검토해보기
    constructor(opening_price:String,closing_price:String,min_price:String,max_price:String,
                units_traded:String,acc_trade_value:String,prev_closing_price:String,units_traded_24H: String,
                acc_trade_value_24H: String,fluctate_24H:String,fluctate_rate_24H:String,date:String)
            :this() {
        this.opening_price = opening_price
        this.closing_price = closing_price
        this.min_price = min_price
        this.max_price = max_price
        this.units_traded = units_traded
        this.acc_trade_value = acc_trade_value
        this.prev_closing_price = prev_closing_price
        this.units_traded_24H = units_traded_24H
        this.acc_trade_value_24H = acc_trade_value_24H
        this.fluctate_24H = fluctate_24H
        this.fluctate_rate_24H = fluctate_rate_24H
        this.date = date
    }
}
