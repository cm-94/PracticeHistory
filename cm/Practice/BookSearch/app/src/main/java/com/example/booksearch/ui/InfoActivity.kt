package com.example.booksearch.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
import androidx.viewpager.widget.ViewPager
=======
import com.example.booksearch.BookFragment
>>>>>>> 55f01baf934b6ce99b7b2220c6e8ab3eeff5bf94
import com.example.booksearch.R
import com.example.booksearch.data.BookLink
import com.example.booksearch.ui.adpater.FragmentAdapter
import com.example.booksearch.util.CommonUtils
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // 인텐트 수신
        val index = intent.getIntExtra(CommonUtils.BOOK_INFO_INDEX,0)

        // Fragment 어댑터 생성
        val fragAdt = FragmentAdapter(supportFragmentManager)
        // 링크 가져와 frament 추가
        BookLink.getLinks().forEachIndexed { index, link ->
<<<<<<< HEAD
            fragAdt.addFragment(BookFragment.newInstance(link,index),link);
        }

        // ViewPager 어댑터 & 첫번째 item(Intent로 전달받은 index로 확인) 설정
        viewPager.adapter = fragAdt
        viewPager.currentItem = index

        // 스크롤 에니메이션 동작 시간 설정
        viewPager!!.setDurationScroll(300)
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                viewPager.scrollState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
=======
            Log.d("asdfasdf", "index: $index, link: $link")
            fAdapter.addFragment(BookFragment.newInstance(link,index),link);
        }



        book_vp.adapter = fAdapter
        book_vp.currentItem = index

>>>>>>> 55f01baf934b6ce99b7b2220c6e8ab3eeff5bf94


        // 스크롤 에니메이션 동작 시간 설정
        book_vp!!.setDurationScroll(300)
//        book_vp!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//
//            override fun onPageScrollStateChanged(state: Int) {
//                book_vp.scrollState = state
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//            override fun onPageSelected(position: Int) {
//
//            }
//
//        })


    }

}