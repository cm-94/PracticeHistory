package com.example.test.adapter
import android.content.Context
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
import java.text.DecimalFormat


class ExpectAdapter(private var context: Context, private var items: ArrayList<ExpectData>) : RecyclerView.Adapter<ExpectAdapter.ExViewHolder>() {
    private val dataFormat = DecimalFormat("#,###.#")
    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(layout)에 TickerData를 각각 할당한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ExViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.expect_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return ExViewHolder(itemView)
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
    override fun onBindViewHolder(holder: ExViewHolder, position: Int) {
        items[position].let { item: ExpectData ->
            if(item.price_type == "1"){ // 주식일 때
                holder.item_type.text = context.getString(R.string.allocation)         // 배당
            }
            else if(item.price_type == "2"){ // 예금일 때
                holder.item_type.text = context.getString(R.string.interest)         // 이자
            }
            else if(item.price_type == "3"){ // 채권일 때
                holder.item_type.text = context.getString(R.string.distrib)         // 분배
            }

            holder.name.text = item.product_name              // 상품명
            holder.due_date.text = item.due_date              // 예상 지급일


            holder.price.text = context.getString(R.string.format_won,dataFormat.format(item.product_price.toFloat()).toString())

            // 클릭 시 화면 전환 -> Toast로 대체
            holder.itemView.setOnClickListener {
                Toast.makeText(context, "${items[position].product_name} 클릭됨!", Toast.LENGTH_SHORT).show()
//                // context(MainActivity) ~> InfoActivity
//                val intent = Intent(context, InfoActivity::class.java)
//                // order_currency : InfoActivity에서 데이터 요청에 쓰일 종목명(Keyword)
//                // Constants.ORDER_CURRENCY == "order_currency"
//                intent.putExtra(Constants.ORDER_CURRENCY, this.items[position].order_currency)
//                // startActivity -> intent에 세팅한대로 Activity 실행.
//                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }
        }
        Log.d("item_bind!!",items[position].product_name);
    }

    class ExViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.item_name)
        val price : TextView = itemView.findViewById(R.id.item_price)
        val item_type : TextView = itemView.findViewById(R.id.item_type)
        val due_date : TextView = itemView.findViewById(R.id.due_date)

    }

    /**
     * @param (MutableList<TickerData>)
     *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
     *  - MainActivity에서 데이터를 수신에 성공하면
     */
    fun addItems(newItems: ArrayList<ExpectData>){
        // items 비우기
        this.items = arrayListOf()
        // 전달받은 items로 다시 세팅
        this.items.addAll(newItems)
    }
}
