package com.example.mvvmpractice

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.data_list_item.view.*

class DataAdapter(
        var items :     ArrayList<Model>,
        var listener: ((Model) -> Unit)? = null
) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.data_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            with(it) {
                holder.rlItem.setOnClickListener {
                    val toast = Toast.makeText(context, "어댑터 -> " + items[position].name + " 클릭!!", Toast.LENGTH_SHORT) // 2초 출력
                    toast.setGravity(Gravity.BOTTOM, 0, 130)
                    toast.show()

                    listener?.invoke(items[position])
                }
                holder.name.text = name          // 이름
                holder.age.text = age.toString() // 나이

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): Model? {
        return items.getOrNull(position)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // 선택View
        val rlItem = v.rlItem
        // 이름
        val name = v.item_name
        // 나이
        val age = v.item_age
    }
}

