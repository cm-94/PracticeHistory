package com.example.booksearch.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
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
        // 1. 사용자가 클릭한 item의 index 받기
        val index = intent.getIntExtra(CommonUtils.BOOK_INFO_INDEX,0)
        // 2. Link 리스트(BookLink) 받기
        val bookLink = intent.getParcelableExtra<BookLink>(CommonUtils.BOOK_INFO_URL)

        // 3. Fragment 어댑터 생성
        val fragAdt = FragmentAdapter(supportFragmentManager)
        // 4. 링크 가져와 frament 추가
        bookLink?.links?.forEachIndexed { index, s ->
            fragAdt.addFragment(BookFragment.newInstance(s,index),s);
        }

        // 5. ViewPager 어댑터 & 첫번째 item(Intent로 전달받은 index로 확인) 설정
        // - Fragment size 체크
        if(fragAdt.count == 0){
            // 생성된 화면이 없으면 에러!! InfoActivity를 종료한다
            /** MainActivity한테 에러로 종료됐다고 전달해주는 부분 추가 고려 */
            finish()
        }
        book_vp.adapter = fragAdt
        book_vp.currentItem = index
        // + 스크롤 에니메이션 동작 설정
        book_vp!!.setDurationScroll(300) // 시간 설정

    }

    // 수신된 인텐트 처리 함수
    //  index, bookLink가 전역변수일 때 사용
    private fun processIntent(intent : Intent){
        // 1. 사용자가 클릭한 item의 index 받기
        val index = intent.getIntExtra(CommonUtils.BOOK_INFO_INDEX,0)

        val bookLink = intent.getParcelableExtra<BookLink>(CommonUtils.BOOK_INFO_URL)
    }
}