package com.example.bit.utils

object Constants {
    /** order_currency */
    const val ALL_CURRENCY = "ALL"
    const val ORDER_CURRENCY = "order_currency"
    const val PAYMENT_CURRENCY = "payment_currency"
    const val EXCHANGE_RATE = "exchange_rate"

    /** payment_currency for Request Param*/
    const val PAYMENT_CURRENCY_KRW = "KRW"
    const val PAYMENT_CURRENCY_USD = "USD"
    const val PAYMENT_CURRENCY_JPY = "JPY"

    /** payment_currency for ticker_name */
    const val KRW = "원화/￦"
    const val USD = "달러/＄"
    const val JPY = "엔화/￥"

    /** timer interval */
    const val TICKER_TIMER = 1000L
}