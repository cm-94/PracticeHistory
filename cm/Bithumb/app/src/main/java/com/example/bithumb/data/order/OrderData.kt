package com.example.bithumb.data.order

import com.example.bithumb.data.order.Order

class OrderData(
    var timestamp: String,
    var payment_currency: String,
    var order_currency: String,
    var bids: ArrayList <Order>,
    var asks: ArrayList<Order>
) {
}