package com.example.test.data

class ExpectData (
    var product_name : String,  // 상품명
    var product_type : String,  // 상품 종류(1: 주식, 2: 예금, 3: 채권)
    var product_price : String, // 상품 가격
    var price_type : String,    // 수익 종류(1: 배당, 2: 이자, 3: 분배)
    var due_date : String,      // 지급기일
    var product_count : String, // 상품 수량
    var product_yield : String, // 수익률
    var product_valuation : String // 평가손익
    ){
}