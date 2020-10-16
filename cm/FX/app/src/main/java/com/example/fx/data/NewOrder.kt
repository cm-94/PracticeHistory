package com.example.fx.data

object NewOrder {
    /** 고가 */
    var maxprice: Float = 0.000F
    /** 저가 */
    var minprice: Float = 0.000F
    /** 시가비 */
    var fluctate: Float = 0.000F


    /** 매도 호가 */
    var sellprice: Float = 0.000F
    /** 매수 호가 */
    var callprice: Float = 0.000F
    /** 스프레드 */
    var spread: Float = 0.0F


    /** 내 잔액 */
    var balance: Int = 0
    /** 주문 총 수량 */
    var callAmount : Int = 0

    /** 주문 단위(1,000 or 10,000) */
    var callUnit : Int = 0

    /** 주문 개별 수량(총 수량/단위) */
    var callUnitCount : Int = 0
    /** 주문 개별 Pips/단위 */
    var pipsCount : Float = 0.0F


    /** 설정된 pips (Switch)*/
    var sellAutoPips:Float = 0.0F
    var buyAutoPips:Float = 0.0F

    /** 설정할 pips (SP Button) */
    var sellPips:Float = 0.000F
    var buyPips:Float = 0.000F

}