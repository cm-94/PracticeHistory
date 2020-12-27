package com.example.booksearch.data

import android.os.Parcel
import android.os.Parcelable

class BookLink(var links : ArrayList<String>) : Parcelable{
    constructor() : this(arrayListOf())

    override fun describeContents(): Int {
        return 0
    }

    // parcelable 객체로 BookLink.links 쓰기
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeStringList(this.links)
    }

    // CREATOR
    companion object CREATOR : Parcelable.Creator<BookLink> {
        override fun createFromParcel(parcel: Parcel): BookLink {
            val links = parcel.createStringArrayList() as ArrayList<String>
            return BookLink(links)
        }
        override fun newArray(size: Int): Array<BookLink?> {
            return arrayOfNulls(size)
        }
    }

    fun add(link : String){
        links.add(link)
    }

    fun addLinks(links : ArrayList<String>){
        links.addAll(links)
    }

    fun clear(){
        links.clear()
    }
}