package com.example.mvvmpractice

import android.view.Display
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var _data = MutableLiveData<ArrayList<Model>>()
    val data : LiveData<ArrayList<Model>> get() = _data
    val items = ArrayList<Model>()

    fun requestNewData(count : Int){
        val dummyData = Model("",0)
        items.addAll(dummyData.requestNewData())
        _data.postValue(items)

    }
}