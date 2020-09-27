package com.example.bit.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bit.R
import com.example.bit.data.TickerMain
import com.example.bit.ui.info.InfoActivity
import com.example.bit.utils.Constants
import kotlinx.android.synthetic.main.ticker_item.view.*
import kotlin.collections.ArrayList

/** MainAdapter( context, item )
 * @param Context List<TickerMain>
 *  : context - 각 View item마다 클릭 이벤트를 세팅. 이때 어떤 Activity(context)에서 이동하는지 알기 위해 필요.
 *    List<TickerMain> - Adapter에서 RecyclerView에 표현할 TickerData(item)들을 담고있는 MutableList.
 */
class MainAdapter(private val context : Context, private var items: ArrayList<TickerMain>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(View)에 TickerData를 각각 연결한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.ticker_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return MainViewHolder(itemView)
    }

    /** - MainAdapter에서 가지고있는(화면에 보여주고 있는) item의 개수를 반환
     * @param none
     * @return Int
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /** @param (holder,positio)
     *  @return none
     *  - items의 position번째 데이터를 MyViewHolder에 세팅한다
     *  - ViewHolder 객체가 재사용될 때 자동 호출
     *  - View item에 대한 클릭 이벤트 추가
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item:TickerMain =items.get(position)
        holder.setItem(item)

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
    }

    // TODO Item을 담아둘 ViewHolder Class 정의!!
    // 해당 View( => 한 블럭) 을 클릭했을 때 InfoActivity로 넘어가도록
    // onClickListener 상속 후 override onClick()
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: View에 표현할 item의 데이터(종목명,시가,종가,저가,고가) 세팅하기
        fun setItem(item: TickerMain) {
            itemView.tickerName.text = item.order_currency        // 종목명
            itemView.opening_Price.inputText(item.opening_price)      // 시가
            itemView.closing_Price.inputText(item.closing_price)      // 종가
            itemView.min_Price.inputText(item.min_price)              // 저가
            itemView.max_Price.inputText(item.max_price)              // 고가
            itemView.fluctate.inputText(item.fluctate_24H)            // 변동
            itemView.fluctate_rate.inputText(item.fluctate_rate_24H)  // 변동률

            // 변동률 -> (%) 붙이기!
            itemView.fluctate_rate.append("%")

            // 변동 & 변동률 => 증,감에 따라 색 다르게!!
            if(itemView.fluctate.text[0] =='-'){ itemView.fluctate.setTextColor(Color.BLUE)}
            else itemView.fluctate.setTextColor(Color.RED)

            if(itemView.fluctate_rate.text[0]=='-') itemView.fluctate_rate.setTextColor(Color.BLUE)
            else itemView.fluctate_rate.setTextColor(Color.RED)
        }
    }

    /** addItems
     * @param (MutableList<TickerData>)
     */
    // 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
    // MainActivity에서 데이터를 수신에 성공하면
    fun addItems(items:ArrayList<TickerMain>){
        // items 비우기
        this.items = arrayListOf()
//        this.items.clear() // -> addAll 해도 items가 반영 안됨..

        // 전달받은 items로 다시 세팅
        this.items.addAll(items)
    }
}
