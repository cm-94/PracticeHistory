package com.example.test.adapter
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.data.ExpectData
import com.example.test.data.MajorData
import java.text.DecimalFormat


class MajorAdapter(private var context: Context, private var items: ArrayList<MajorData>) : RecyclerView.Adapter<MajorAdapter.MajorViewHolder>() {
    private val dataFormat = DecimalFormat("#,###.#")
    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(layout)에 TickerData를 각각 할당한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MajorAdapter.MajorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.major_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return MajorAdapter.MajorViewHolder(itemView)
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
    override fun onBindViewHolder(holder: MajorAdapter.MajorViewHolder, position: Int) {
        items[position].let { item: MajorData ->
            holder.name.text = item.product_name        // 종목명
            holder.market.text = item.product_market    // 마켓
            holder.count.text = context.getString(R.string.stock_count,dataFormat.format(item.product_count.toInt()).toString())// 보유량

            // 평가금액 ( 3억 넘나 비교!! ) -> 2021년 4월부터 1억
            holder.price.text = context.getString(R.string.txt_won,dataFormat.format(item.product_price.toLong()).toString())
            if(item.product_price.toLong() > 300000000){
                holder.price.typeface = Typeface.DEFAULT_BOLD // Bold처리
                Log.d("MajorAdapter","종목명: ${item.product_name}, 가격:${item.product_price}")
            }


            // 지분율 ( 1% 넘나 비교!! ) -> 코스닥 : 2%
            holder.stake.text = context.getString(R.string.per,dataFormat.format(item.product_stake.toFloat()).toString())
            holder.stake.text = context.getString(R.string.per,item.product_stake)
            if(item.product_stake.toFloat() > 1){
                holder.stake.typeface = Typeface.DEFAULT_BOLD // Bold처리
                Log.d("MajorAdapter","종목명: ${item.product_name}, 지분율:${item.product_price}")
            }
        }
    }

    class MajorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.item_name)     // 종목명
        val market : TextView = itemView.findViewById(R.id.item_market) // 마켓
        val count : TextView = itemView.findViewById(R.id.item_count)   // 보유량

        val price : TextView = itemView.findViewById(R.id.item_price)   // 평가금액
        val stake : TextView = itemView.findViewById(R.id.item_stake)   // 지분율
    }

    /**
     * @param (MutableList<TickerData>)
     *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
     *  - MainActivity에서 데이터를 수신에 성공하면
     */
    fun addItems(newItems: ArrayList<MajorData>){
        // items 비우기
        this.items = arrayListOf()
        // 전달받은 items로 다시 세팅
        this.items.addAll(newItems)
    }
}
