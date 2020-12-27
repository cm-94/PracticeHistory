package com.example.booksearch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/*
@SerializedName - Json의 field와 Class의 property를 매칭시켜준다.
@Expose         - object의 해당 값이 null일 경우 매칭을 생략한다.
*/
class BookData (
    @SerializedName("total")
    @Expose
    val total: Int,
    @SerializedName("items")
    @Expose
    val items : List<BookItem>,
    @SerializedName("lastBuildDate")
    @Expose
    val lastBuildDate: String,
    @SerializedName("start")
    @Expose
    val start: Int,
    @SerializedName("display")
    @Expose
    val display: Int)
{
    override fun toString(): String {
        return "lastBuildDate: $lastBuildDate, total: $total, start: $start, display: $display, items.size: $items.size"
    }
}