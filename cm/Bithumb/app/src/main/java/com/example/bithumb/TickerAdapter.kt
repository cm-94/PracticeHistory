package com.example.bithumb

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bit.utils.Constants
import com.example.bithumb.data.TickerData
import kotlinx.android.synthetic.main.ticker_item.view.*

class TickerAdapter(private var context :Context,private var items: ArrayList<TickerData>) : RecyclerView.Adapter<TickerAdapter.MainViewHolder>() {
    private var prevItems : ArrayList<TickerData> = arrayListOf()

    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(layout)에 TickerData를 각각 할당한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.ticker_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return MainViewHolder(itemView)
    }

    /**
     * @param none
     * @return Int
     *  - MainAdapter에서 가지고있는(화면에 보여주고 있는) item의 개수를 반환
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * @param (holder,positio)
     * @return none
     *  - items의 position번째 데이터를 MyViewHolder에 세팅한다
     *  - ViewHolder 객체가 재사용될 때 자동 호출
     *  - View item에 대한 클릭 이벤트 추가
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        /**
         *  변동값의 증감에 따라 item View의 배경색이 바뀐다
         * 증가 -> Color.RED
         * 감소 -> Color.BLUE
         * 동일 -> Color.White
         */
        val item:TickerData =items.get(position)

        // TODO : 환율 변동 case 추가하기
//        if(prevItems.get(position).fluctate_24H.toFloat()==items.get(position).fluctate_24H.toFloat()){
////            Log.d("MainAdapter_back","Change Exchange Rate  => "+holder.itemView.fluctate.text.toString() +", "+ item.fluctate_rate_24H )
//            return
//        }
        if(prevItems.size>0) {

            // 변동값 -> 감소
            items[position].let { item: TickerData ->
                if (item.exchange_rate != prevItems[position].exchange_rate) {
                    holder.itemView.setBackgroundColor(Color.WHITE)
                } else if (prevItems[position].fluctate_24H.toFloat() > item.fluctate_24H.toFloat()) {
                    holder.itemView.setBackgroundColor(Color.BLUE)
                } else if (prevItems[position].fluctate_24H.toFloat() < item.fluctate_24H.toFloat()) {
                    holder.itemView.setBackgroundColor(Color.RED)
                }// 변동값 -> 일정
                else {
                    holder.itemView.setBackgroundColor(Color.WHITE)
                }// holder.itemView.setBackgroundColor(Color.argb(20,0,100,100))

                holder.order_currency.text = item.order_currency
                holder.closing_price.inputText(item.closing_price)             // 종가
                holder.min_price.inputText(item.min_price)                     // 저가
                holder.max_price.inputText(item.max_price)                     // 고가
                holder.units_traded_24H.inputText(item.units_traded_24H)       // 거래량
                holder.acc_trade_value_24H.inputText(item.acc_trade_value_24H) // 거래금액
                holder.fluctate.inputText(item.fluctate_24H)                   // 변동
                holder.fluctate_24H.inputText(item.fluctate_rate_24H)          // 변동률
                // 변동률 -> (%) 붙이기!
                holder.fluctate_24H.append("%")

                // 변동 & 변동률 => 증,감에 따라 TextColor 다르게!! => '-'로 구분
                if (holder.fluctate.text[0] == '-') {
                    holder.fluctate.setTextColor(Color.BLUE)
                } else holder.fluctate.setTextColor(Color.RED)

                if (holder.fluctate_24H.text[0] == '-') holder.fluctate_24H.setTextColor(Color.BLUE)
                else holder.fluctate_24H.setTextColor(Color.RED)

            }

            holder.itemView.setOnClickListener {
//                // context(MainActivity) ~> InfoActivity
//                val intent = Intent(context, InfoActivity::class.java)
//                // order_currency : InfoActivity에서 데이터 요청에 쓰일 종목명(Keyword)
//                // Constants.ORDER_CURRENCY == "order_currency"
//                intent.putExtra(Constants.ORDER_CURRENCY, this.items[position].order_currency)
//                // startActivity -> intent에 세팅한대로 Activity 실행.
//                // TODO : startActivity option값 살펴보기
//                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }
        }
    }

    // TODO Item을 담아둘 ViewHolder Class 정의!!
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val order_currency = itemView.tickerName
        val closing_price = itemView.closing_Price
        val min_price = itemView.min_Price
        val max_price = itemView.max_Price
        val units_traded_24H = itemView.units_traded_24H
        val acc_trade_value_24H = itemView.acc_trade_value_24H
        val fluctate = itemView.fluctate
        val fluctate_24H = itemView.fluctate_rate

    }

    /**
     * @param (MutableList<TickerData>)
     *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
     *  - MainActivity에서 데이터를 수신에 성공하면
     */
    fun addItems(items:ArrayList<TickerData>){
        // items 비우기
        prevItems =  this.items
        this.items = arrayListOf()
        // 전달받은 items로 다시 세팅
        this.items.addAll(items)
    }
}
