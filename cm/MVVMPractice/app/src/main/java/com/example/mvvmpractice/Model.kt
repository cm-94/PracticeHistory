package com.example.mvvmpractice

class Model{
    var name : String = ""
    var age : Int = 0

    constructor(name : String, age: Int){
        this.name = name
        this.age = age
    }

    fun requestNewData() : ArrayList<Model>{
        val arr = ArrayList<Model>()
        arr.add(Model("창민",13))
        arr.add(Model("민창",14))
        arr.add(Model("임창민",19))
        arr.add(Model("홍길동",41))
        arr.add(Model("장길산",12))

        return arr
    }
}
