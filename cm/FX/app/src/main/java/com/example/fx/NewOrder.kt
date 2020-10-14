package com.example.fx

object NewOrder {
    /** 고가 */
    var maxprice: Float = 105.597F
    /** 저가 */
    var minprice: Float = 105.453F
    /** 시가비 */
    var fluctate: Float = 0.042F




    /** 매도 호가 */
    var sellprice: Float = 105.498F
    /** 매수 호가 */
    var callprice: Float = 105.503F
    /** 스프레드 */
    var spread: Float = 0.5F


    /** 내 잔액 */
    var balance: Int = 500000

    /** 주문 단위(1,000 or 10,000) */
    var callAmount : Int = 0


}