package com.example.booksearch.data

import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.time.LocalDateTime
import java.util.*

data class BookData(
    @SerializedName("lastBuildDate")
    val lastBuildDate: Date,
    @SerializedName("total")
    val total: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: ArrayList<BookItem>)
{
    override fun toString(): String {
        return "lastBuildDate: $lastBuildDate, total: $total, start: $start, display: $display, items.size: $items.size"
    }
}