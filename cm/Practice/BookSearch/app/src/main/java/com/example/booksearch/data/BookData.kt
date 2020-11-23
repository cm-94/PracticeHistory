package com.example.booksearch.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BookData(
    @SerializedName("lastBuildDate")
    @Expose
    val lastBuildDate: String,
    @SerializedName("total")
    @Expose
    val total: Int,
    @SerializedName("start")
    @Expose
    val start: Int,
    @SerializedName("display")
    @Expose
    val display: Int,
    @SerializedName("items")
    @Expose
    val items : List<BookItem>)
{
    fun getBookItems() : List<BookItem>{
        return items

    }
//    fun getItems() : List<BookItem> {
//        return this.items
//    }
    override fun toString(): String {
        return "lastBuildDate: $lastBuildDate, total: $total, start: $start, display: $display, items.size: $items.size"
    }
}