package com.example.booksearch.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * title        책 제목
 * publisher    출판사
 * author       저자
 * price        가격
 * image        이미지(URL)
 * link         상세정보(URL)
 */
class BookItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("publisher")
    val publisher: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun toString(): String {
        return "title: $title, link: $link, image: $image, author: $author, price: $price"
        /*+"discount: $discount, publisher: $publisher, pubdate: $pubdate, isbn: $isbn, description: $description"*/
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(title)
        p0?.writeString(link)
        p0?.writeString(image)
        p0?.writeString(author)
        p0?.writeString(price)
        p0?.writeString(publisher)
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }
}

