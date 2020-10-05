package com.example.bit.custom

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bit.R

class ExAdapter(context: Context, resource: ArrayList<Exchange>) :
    ArrayAdapter<Exchange>(context, 0,resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("Adpater","getView Called")
        return initView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("Adpater","getDropDownView Called")
        return initView(position,convertView,parent)
    }


    /**
     * @param
     * @return
     */
    fun initView(position:Int,convertView:View?,parent:ViewGroup) :View{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.spinner_item,parent,false)
        Log.d("Adpater","init Called")
        val imageViewFlag : ImageView = view.findViewById(R.id.exchange_rate)
        val textView : TextView = view.findViewById(R.id.exchange_text)

        val currentItem : Exchange? = getItem(position)
        imageViewFlag.setImageResource(currentItem?.exchange_rate?:0)
        textView.text = currentItem?.exchange_text
        return view
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getItem(position: Int): Exchange? {
        return super.getItem(position)
    }

    override fun add(`object`: Exchange?) {
        super.add(`object`)
    }

    override fun getPosition(item: Exchange?): Int {
        return super.getPosition(item)
    }
}