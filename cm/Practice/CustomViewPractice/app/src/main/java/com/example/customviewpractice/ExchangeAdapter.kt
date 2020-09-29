package com.example.customviewpractice

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ExchangeAdapter(context: Context, resource: ArrayList<ExchangeRate>) :
    ArrayAdapter<ExchangeRate>(context, 0,resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("Adpater","getView Called")
        return initView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("Adpater","getDropDownView Called")
        return initView(position,convertView,parent)
    }


    fun initView(position:Int,convertView:View?,parent:ViewGroup) :View{
        val inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.custom_textview,parent,false)
        Log.d("Adpater","init Called")
        var imageViewFlag : ImageView = view.findViewById(R.id.exchange_rate)
        var textView : TextView = view.findViewById(R.id.exchange_text)

        var currentItem : ExchangeRate? = getItem(position)
        imageViewFlag.setImageResource(currentItem?.exchange_rate?:0)
        textView.setText(currentItem?.exchange_text)
        return view
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getItem(position: Int): ExchangeRate? {
        return super.getItem(position)
    }

    override fun add(`object`: ExchangeRate?) {
        super.add(`object`)
    }

    override fun getPosition(item: ExchangeRate?): Int {
        return super.getPosition(item)
    }


}