package com.example.booksearch.ui.adpater

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.booksearch.R
import com.example.booksearch.data.BookItem
import com.example.booksearch.ui.InfoActivity
import com.example.booksearch.util.CommonUtils
import kotlinx.android.synthetic.main.book_item.view.*
import java.text.DecimalFormat
import java.text.NumberFormat


class BookAdapter(private val context: Context, private var items: ArrayList<BookItem>) : RecyclerView.Adapter<BookAdapter.MainViewHolder>() {
    var formatter: NumberFormat = DecimalFormat("#,###")

    /** onCreateViewHolder()
     *  ViewHolder 객체가 만들어질 때 자동 호출
     *  - MyViewHolder 클래스를 통해 ticker_item(layout)에 TickerData를 각각 할당한다.
     */
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // 인플레이션을 통해 View 객체 만들기
        val itemView: View = inflater.inflate(R.layout.book_item, parent, false)

        // 뷰홀더 객체를 생성하면서 View 객체를 전달하고 그 ViewHolder 객체를 return!!
        return MainViewHolder(itemView)
    }

    /**
     * - MainAdapter에서 가지고있는(화면에 보여주고 있는) item의 개수를 반환
     * @return Int
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * @param (holder,position)
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
        // TODO : 환율 변동 case 추가하기( =>onBindViewHolder(...payload)에서 처리!! )
        Log.d("MainActivity_BindView", "item:" + items[position].title + ", position: " + position)
        items[position].let { item: BookItem ->
            holder.title.text = item.title
            holder.publisher_author.text = context.getString(
                R.string.publisher_author,
                item.publisher,
                item.author
            )
            holder.price.text = context.getString(R.string.price, formatter.format(item.price))
            holder.test.text = item.image
            holder.link = item.link


            // TODO : Glide 응답 Header -> content-length == 0 일 때 처리 필요..(빈 이미지)
            if (!item.image.equals("")){
                Glide.with(context).load(item.image)./*override(item.image.length).*/into(holder.img_book)
                holder.img_book.visibility = View.VISIBLE
                holder.test.visibility = View.VISIBLE
            }else{
                holder.img_book.visibility = View.GONE
                holder.test.visibility = View.GONE
            }

            holder.img_book.setOnClickListener{
//                Toast.makeText(context,"${holder.title.text} 이미지 클릭!, ${Glide.with(context).load(item.image)}",Toast.LENGTH_SHORT).show()
                Toast.makeText(context,"${holder.title.text} 클릭!, 링크 : ${holder.link}",Toast.LENGTH_SHORT).show()
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra(CommonUtils.BOOK_INFO_URL,holder.link)
                ContextCompat.startActivity(context,intent,null)

                Toast.makeText(
                    context,
                    "${items[position].title}클릭됨! Link: ${items[position].link}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // TODO Item을 담아둘 ViewHolder Class 정의!!
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img_book = itemView.img_book
        val title = itemView.title_tv
        val price = itemView.price_tv
        val publisher_author = itemView.publisher_author_tv
        var link = ""
        val test = itemView.test
    }

    /**
     * @param (MutableList<TickerData>)
     *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
     *  - MainActivity에서 데이터를 수신에 성공하면
     */
    fun addItems(newItem: ArrayList<BookItem>){
        // items 비우기
//        this.items = arrayListOf()
        // 전달받은 items로 다시 세팅
        Log.d("MainActivity_BindAdd", "Before items.size: " + items.size)
        items=newItem
        Log.d("MainActivity_BindAdd", "After items.size: " + items.size)

    }
}
