package com.example.booksearch.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.booksearch.BookFragment
import com.example.booksearch.R
import com.example.booksearch.data.BookLink
import com.example.booksearch.ui.adpater.FragmentAdapter
import com.example.booksearch.util.CommonUtils
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // TODO : 웹뷰 클릭 이벤트 처리
        // TODO : 화면 길이..(naver 가면 길이 넘 길어짐..)
        // TODO : 뒤로가기(backpress) 이벤트 처리
        val index = intent.getIntExtra(CommonUtils.BOOK_INFO_INDEX,0)
//
//        // 자바스크립트가 동작할 수 있도록 세팅
//        val webSetting = webview.settings
//        webSetting.javaScriptEnabled = true
//        // 웹뷰 클라이언트 설정
//        webview.webViewClient = BookWebView()
//        // 웹뷰 실행
//        webview.loadUrl(url)



        var fAdapter = FragmentAdapter(supportFragmentManager)

        BookLink.getLinks().forEachIndexed { index, link ->
            Log.d("asdfasdf", "index: $index, link: $link")
            fAdapter.addFragment(BookFragment.newInstance(link,index),link);
        }



        book_vp.adapter = fAdapter
        book_vp.currentItem = index



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