    package com.example.booksearch.ui.adpater

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.booksearch.R
import com.example.booksearch.data.BookItem
import com.example.booksearch.ui.InfoActivity
import com.example.booksearch.util.Constants
import kotlinx.android.synthetic.main.book_item.view.*
import java.text.DecimalFormat
import java.text.NumberFormat


class BookAdapter(private val context: Context, private val items: MutableList<BookItem>) : RecyclerView.Adapter<BookAdapter.MainViewHolder>() {
    // 책 가격 formatting 변수
    private val formatter : NumberFormat = DecimalFormat("#,###")

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
        items[position].let { item: BookItem ->
            holder.title.text = item.title.replace(Constants.HTML_TAG.toRegex(),"")
            holder.publisherAuthor.text = context.getString(
                R.string.publisher_author,
                item.publisher.replace(Constants.HTML_TAG.toRegex(),""),
                item.author.replace(Constants.HTML_TAG.toRegex(),"")
            )

            holder.price.text = context.getString(
                R.string.price,
                formatter.format(item.price.toInt()))
            holder.bookImg.visibility = View.VISIBLE
            holder.link = item.link

            // 이미지 불러오기 -> Glide 응답 처리 => RequestListener
            // 성공 : VISIBLE, 실패 : GONE
            Glide.with(context)
                .load(item.image)
                .listener(object : RequestListener<Drawable?> {
                    // 이미지 로드 성공 시
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.bookImg.visibility = View.VISIBLE         // 이미지 View -> Visible
                        return false
                    }// 이미지 로드 실패 시
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.bookImg.visibility = View.GONE            // 이미지 View -> Gone
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(200)) // 이미지 에니메이션(Fade)
                .into(holder.bookImg)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, InfoActivity::class.java)
                // 1. 사용자가 클릭한 index(position) intent에 담기
                intent.putExtra(Constants.BOOK_INFO_INDEX, position)
                // 2. 검색된 책들의 Link 담기( => BookLink : Parcelable )
                val bookLinks = arrayListOf<String>()
                items.forEach {
                    bookLinks.add(it.link)
                }
                // 3. Link(List) intent에 담기
                intent.putStringArrayListExtra(Constants.BOOK_INFO_URL, bookLinks)
                // 4. 상세화면 시작하기
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImg: ImageView = itemView.img_book
        val title: TextView = itemView.title_tv
        val price: TextView = itemView.price_tv
        val publisherAuthor: TextView = itemView.publisher_author_tv
        var link: String = ""
    }

    /**
     * @param (MutableList<TickerData>)
     *  - 서버로부터 MutableList<TickerMain> 데이터를 받으면 해당 리스트로 RecyclerView 초기화
     *  - MainActivity에서 데이터를 수신에 성공하면
     */




}
