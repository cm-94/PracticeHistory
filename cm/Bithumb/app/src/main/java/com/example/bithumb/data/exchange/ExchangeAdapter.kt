package com.example.bithumb.data.exchange

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bithumb.R

class ExchangeAdapter(context: Context, resource: ArrayList<ExchangeSpinner>)
    : ArrayAdapter<ExchangeSpinner>(context, 0, resource) {

    /** */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("Spinner Adapter","getView 실행됨" )
        return initView(position, parent)
    }

    /** spinner의 아래 화살표 클릭(목록보기)시 실행됨*/
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }


    /**
     * exchange_item의 각 view(text,image)에 데이터 할당
     */
    fun initView(position: Int, parent: ViewGroup) : View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exchange_item,parent,false)
        val imageViewFlag : ImageView = view.findViewById(R.id.exchange_image)
        val textView : TextView = view.findViewById(R.id.exchange_text)

        val currentItem : ExchangeSpinner? = getItem(position)
        imageViewFlag.setImageResource(currentItem?.exchangeImage?:0)
        textView.text = currentItem?.exchangeText
        return view
    }
}