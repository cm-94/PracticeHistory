package com.example.test.adapter
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.data.ExpectData
import java.text.DecimalFormat

class TaxAdapter(private var context: Context, private var items: ArrayList<ExpectData>) : RecyclerView.Adapter<TaxAdapter.TaxViewHolder>() {
    private val dataFormat = DecimalFormat("#,###.#")
    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(layout)에 TickerData를 각각 할당한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): TaxViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.tax_calc_item, parent, false)
        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return TaxViewHolder(itemView)
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
    override fun onBindViewHolder(holder: TaxViewHolder, position: Int) {
        items[position].let { item: ExpectData ->
            // TODO => 예금 데이터 거르기
            if(item.product_type == "2"){ //  예금 -> 포함 x
                return
            }
            else if(item.product_type == "1"){ // 주식 ( 매도, 주 )
                holder.item_type.text = context.getString(R.string.sell_stock)
                holder.item_count_type.text = context.getString(R.string.stock)

            }
            else if(item.product_type == "3"){ // 채권 ( 환매, 좌 )
                holder.item_type.text = context.getString(R.string.sell_che)
                holder.item_count_type.text = context.getString(R.string.che)
            }

            // 상품명
            holder.item_name.text = item.product_name

            // 평가손익
            if(item.product_valuation.toInt() > 0){
                holder.item_valuation.setTextColor(Color.RED)
                holder.item_valuation.text = context.getString(R.string.plus,dataFormat.format(item.product_valuation.toInt()).toString())
            }else{
                holder.item_valuation.setTextColor(Color.BLUE)
                holder.item_valuation.text = context.getString(R.string.minus,dataFormat.format(item.product_valuation.toInt()).toString())
            }

            // 보유수량
            if(item.product_type == "1"){
                // (~주)
                holder.item_count.text = context.getString(R.string.tax_stock_count,dataFormat.format(item.product_count.toInt()).toString())
            }else{
                // (~좌)
                holder.item_count.text = context.getString(R.string.tax_bond_count,dataFormat.format(item.product_count.toInt()).toString())
            }

            // 수익률
            holder.item_yield.text = context.getString(R.string.per,item.product_yield)

            // X 버튼 클릭 시 입력값 초기화
            holder.btn_delete_exp.setOnClickListener {
                holder.item_edit.setText("0")
            }

            // TODO :최대 버튼 클릭 시 입력값 최대
            holder.btn_max.setOnClickListener {
                holder.item_edit.setText("100,000")
            }
        }

    }



    class TaxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_name : TextView = itemView.findViewById(R.id.item_name)            // 상품명
        val item_count : TextView = itemView.findViewById(R.id.item_count)          // 보유량
        val item_valuation : TextView = itemView.findViewById(R.id.item_valuation)  // 평가손익
        val item_yield : TextView = itemView.findViewById(R.id.item_yield)          // 수익률

        val item_type : TextView = itemView.findViewById(R.id.item_type)            // 상품 타입( 주식 : 매도, 채권 : 환매 )
        val item_edit : EditText = itemView.findViewById(R.id.edit_input)           // 사용자 입력값

        val item_count_type : TextView = itemView.findViewById(R.id.item_count_type)// 상품 단위( 주, 좌)
        val btn_delete_exp : TextView = itemView.findViewById(R.id.btn_delete_exp)  // x 버튼
        val btn_max : TextView = itemView.findViewById(R.id.btn_max)                  // 최대 버튼

    }

}