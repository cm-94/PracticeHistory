package com.example.bit.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bit.MyTextView
import com.example.bit.R
import com.example.bit.data.TickerMain
import com.example.bit.ui.info.InfoActivity
import com.example.bit.utils.Constants
import kotlinx.android.synthetic.main.ticker_item.view.*
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

/** MainAdapter( context, item )
 * @param Context List<TickerMain>
 *  : context - 각 View item마다 클릭 이벤트를 세팅. 이때 어떤 Activity(context)에서 이동하는지 알기 위해 필요.
 *    List<TickerMain> - Adapter에서 RecyclerView에 표현할 TickerData(item)들을 담고있는 MutableList.
 */
class MainAdapter(private val context : Context, private var items: ArrayList<TickerMain>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private lateinit var prevItems : ArrayList<TickerMain>

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
        val item:TickerMain =items.get(position)

        // TODO : 환율 변동 case 추가하기
//        if(prevItems.get(position).fluctate_24H.toFloat()==items.get(position).fluctate_24H.toFloat()){
////            Log.d("MainAdapter_back","Change Exchange Rate  => "+holder.itemView.fluctate.text.toString() +", "+ item.fluctate_rate_24H )
//            return
//        }
        // 변동값 -> 감소
        if (prevItems.get(position).fluctate_24H.toFloat()>items.get(position).fluctate_24H.toFloat()){
            holder.itemView.setBackgroundColor(Color.BLUE)
        }// 변동값 -> 증가
        else if (prevItems.get(position).fluctate_24H.toFloat()<items.get(position).fluctate_24H.toFloat()){
            holder.itemView.setBackgroundColor(Color.RED)
        }// 변동값 -> 일정
        else{
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
        if(holder.fluctate.text[0] =='-'){ holder.fluctate.setTextColor(Color.BLUE)}
        else holder.fluctate.setTextColor(Color.RED)

        if(holder.fluctate_24H.text[0]=='-') holder.fluctate_24H.setTextColor(Color.BLUE)
        else holder.fluctate_24H.setTextColor(Color.RED)

        holder.itemView.setOnClickListener {
            // context(MainActivity) ~> InfoActivity
            val intent = Intent(context,InfoActivity::class.java)
            // order_currency : InfoActivity에서 데이터 요청에 쓰일 종목명(Keyword)
            // Constants.ORDER_CURRENCY == "order_currency"
            intent.putExtra(Constants.ORDER_CURRENCY,this.items[position].order_currency)
            // startActivity -> intent에 세팅한대로 Activity 실행.
            // TODO : startActivity option값 살펴보기
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }
//        holder.setItem(item)
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

        // TODO: View에 표현할 item의 데이터(종목명,시가,종가,저가,고가) 세팅하기
//        fun setItem(item: TickerMain) {
//            itemView.tickerName.text = item.order_currency            // 종목명
//
//            itemView.closing_Price.inputText(item.closing_price)      // 종가
//            itemView.min_Price.inputText(item.min_price)              // 저가
//            itemView.max_Price.inputText(item.max_price)              // 고가
//
//            itemView.units_traded_24H.inputText(item.units_traded_24H)              // 거래량
//            itemView.acc_trade_value_24H.inputText(item.acc_trade_value_24H)              // 거래금액
//
//            itemView.fluctate.inputText(item.fluctate_24H)            // 변동
//            itemView.fluctate_rate.inputText(item.fluctate_rate_24H)  // 변동률
//

//            // ViewHolder에서 새로 item을 받으면 새로 itemView에 할당할 때
//            // 똑같은 index(position)의 itemView와 대조되지 않기때문에
//            // 이전값과 현재값의 증-감을 비교하기 어렵다..
//            /** 이전값 비교(실패1)
//             * itemView.text - item.data 비교 시도
//             * itemView.text => 항상 0 ..
//             */
////            if(itemView.fluctate.text.toString().toFloat() > item.fluctate_rate_24H.toFloat()){
////                Log.d("MainAdapter_back","Compare: -1 => "+itemView.fluctate.text.toString() +", "+ item.fluctate_rate_24H )
////                itemView.setBackgroundColor(Color.BLUE)
////            }else if(itemView.fluctate.text.toString().toFloat() < item.fluctate_rate_24H.toFloat()){
////                Log.d("MainAdapter_back","Compare: 1 =>"+itemView.fluctate.text.toString() +", "+ item.fluctate_rate_24H )
////                itemView.setBackgroundColor(Color.RED)
////            }else{
////                Log.d("MainAdapter_back","Compare: 0 =>"+itemView.fluctate.text.toString() +", "+ item.fluctate_rate_24H )
////                itemView.setBackgroundColor(Color.WHITE)
////            }
//
//            /** 이전값 비교(실패2)
//             * Custom TextView(MyTextView)에서 previous Text를 가지고 있다가 비교하기
//             * .inputText() 메서드로 TextView가 Background color 변경 시도
//             * 문제1. thread over
//             *  - CoroutineScope, GlobalScope -> 마찬가지..
//             * 문제2. Main Thread 외부에서 View 변경 시도 -> 불가능..
//             *
//             */
////            when(itemView.fluctate.inputText(item.fluctate_24H)){
////                1->{
////                    Log.d("MainAdapter_back","Compare: 1")
////                    itemView.setBackgroundColor(Color.RED)
////                }
////                -1->{
////                    Log.d("MainAdapter_back","Compare: -1")
////                    itemView.setBackgroundColor(Color.BLUE)
////                }
////                0->{
////                    Log.d("MainAdapter_back","Compare: 0")
////                    itemView.setBackgroundColor(Color.WHITE)
////                }
//
////                CoroutineScope(Dispatchers.Default).launch {
////                    itemView.setBackgroundColor(Color.BLUE)
////                }
////            }
//
//            // 변동률 -> (%) 붙이기!
//            itemView.fluctate_rate.append("%")
//
//            // 변동 & 변동률 => 증,감에 따라 색 다르게!!
//            if(itemView.fluctate.text[0] =='-'){ itemView.fluctate.setTextColor(Color.BLUE)}
//            else itemView.fluctate.setTextColor(Color.RED)
//
//            if(itemView.fluctate_rate.text[0]=='-') itemView.fluctate_rate.setTextColor(Color.BLUE)
//            else itemView.fluctate_rate.setTextColor(Color.RED)
//        }

        /** 이전값 비교(실패3)
         * ViewHolder에서 Thread 만들어서 배경색 변경 시도
         * 마찬가지로 Thread 초과..
         */
//        fun setItemBackground(textView: MyTextView,text:String){
//            when(textView.inputText(text)){
//                1->{
//                    GlobalScope.launch{
//                        textView.setBackgroundColor(Color.BLUE)
//                        Thread.sleep(200)
//                        textView.setBackgroundColor(Color.WHITE)
//                    }
//                }
//                -1->{
//                    GlobalScope.launch{
//                        textView.setBackgroundColor(Color.RED)
//                        Thread.sleep(200)
//                        textView.setBackgroundColor(Color.WHITE)
//                    }
//                }
//                0->{}
//            }
//        }
    }

    /**
    * @param (MutableList<TickerData>)
    *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
    *  - MainActivity에서 데이터를 수신에 성공하면
    */
    fun addItems(items:ArrayList<TickerMain>){
        // items 비우기
        prevItems =  this.items
        this.items = arrayListOf()
//        this.items.clear() // -> addAll 해도 items가 반영 안됨..
        // 전달받은 items로 다시 세팅
        this.items.addAll(items)
    }
}
