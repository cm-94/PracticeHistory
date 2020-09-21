package com.example.bit.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.bit.R
import com.example.bit.data.TickerData
import com.example.bit.ui.info.InfoActivity
import kotlinx.android.synthetic.main.ticker_item.view.*

class MainAdapter(private var items: ArrayList<TickerData>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    // items = Adapter에서 RecyclerView에 표현할 TickerData(item)들을 담고있는 MutableList


    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder를 통해 ticker_item(View)에 TickerData를 각각 연결한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.ticker_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return MainViewHolder(itemView)
    }

    /** getItemCount()
     *  - MainAdapter에서 가지고있는(화면에 보여주고 있는) item의 개수를 반환
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /** onBindViewHolder()
     *  @param (holder,positio)
     *  @return none
     *  - items의 position번째 데이터를 MyViewHolder에 세팅한다
     *  - ViewHolder 객체가 재사용될 때 자동 호출
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item:TickerData =items.get(position)
        holder.setItem(item)
    }


    // TODO Item을 담아둘 ViewHolder Class 정의!!
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var view: View = itemView

        //  ViewHolder 생성자로 전달되는 View 객체 참조하기
        init {
            itemView.setOnClickListener(this)
        }
        // TODO: View에 표현할 item의 데이터(종목명,시가,종가,저가,고가) 세팅하기
        fun setItem(item: TickerData) {
            itemView.tickerName.text = item.order_currency                  // 종목명
            itemView.opening_Price.text = item.opening_price  // 시가
            itemView.closing_Price.text = item.closing_price  // 종가
            itemView.min_Price.text = item.min_price          // 저가
            itemView.max_Price.setText(item.max_price)          // 고가
        }

        override fun onClick(p: View?) {
            Log.d("RecyclerView", "CLICK!")
        }
    }

    /** addItems
     * @param (MutableList<TickerData>
     */
    // 서버로부터 NutableList<Ticker> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
    // MainActivity에서 데이터를 수신에 성공하면
    fun addItems(items:ArrayList<TickerData>){
        // items 비우기
        this.items = arrayListOf()
        // 전달받은 items로 다시 세팅
        this.items.addAll(items)
    }

}