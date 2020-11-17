package com.example.booksearch.data

/**
 * title        책 제목
 * publisher    출판사
 * author       저자
 * price        가격
 * image        이미지(URL)
 * link        상세정보(URL)
 */
data class BookItem(
    val title: String,
    val publisher: String,
    val author: String,
    val price: Int,
    val image: String,
    val link: String
) {
    override fun toString(): String {
        return "title: $title, publisher: $publisher, author: $author, price: $price, image: $image"
    }
}

