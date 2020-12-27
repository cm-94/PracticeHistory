package com.example.booksearch.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.booksearch.R
import com.example.booksearch.ui.adpater.FragmentAdapter
import com.example.booksearch.util.Constants
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    private var bookLink = arrayListOf<String>()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        Log.d("LifeCycle_Info", "onCreate() 호출!!")

        // 1. savedInstanceState 확인
        if (savedInstanceState == null){
            Log.d("LifeCycle_Info", "savedInstanceState: null")
            // 2. 인텐트 확인
            if(!processIntent(intent)){
                Log.d("LifeCycle_Info", "intent: null")
                // 전달받은 링크 0개일 때
                finish()
            }
        }else{
            savedInstanceState.let {
                index = savedInstanceState.getInt(Constants.BOOK_INFO_INDEX, 0)
                Log.d("LifeCycle_Info", "Bundle_Index : $index")
                savedInstanceState.getStringArrayList(Constants.BOOK_INFO_URL)?.let {
                    bookLink.clear()
                    bookLink.addAll(it)
                }
                bookLink.forEach {
                    Log.d("LifeCycle_Info", "bookLink.link : $it")
                }
            }
        }
//        // 2. 인텐트 확인
//        if(!processIntent(intent)){
//            // 전달받은 링크 0개일 때
//            finish()
//        }

        // 3. view 초기화
        initView()
    }

    // 수신된 인텐트 처리 함수
    //  index, bookLink가 전역변수일 때 사용
    private fun processIntent(intent: Intent) : Boolean{
        // 인텐트 수신
        // 1. 사용자가 클릭한 item의 index 받기
        index = intent.getIntExtra(Constants.BOOK_INFO_INDEX, 0)
        Log.d("LifeCycle_Info", "Intent_Index : $index")
        // 2. Link 리스트(BookLink) 받기
        intent.getStringArrayListExtra(Constants.BOOK_INFO_URL)?.let { bookLink = it }

        // 3. 전달받은 링크가 0개면 false, 아니면 true 리턴!!
        return bookLink.size != 0
    }

    // 데이터 저장!!
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("LifeCycle_Info", "onSaveInstanceState() 호출!!")
        // 현재 인덱스 저장
        outState.putInt(Constants.BOOK_INFO_INDEX, index)
        // 현재 검색창 입력 값 저장
        outState.putStringArrayList(Constants.BOOK_INFO_URL, bookLink)
        // 현재 total & start 저장

        super.onSaveInstanceState(outState)
    }

    private fun initView(){
        val nightModeFlags: Int = applicationContext.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            // DarkMode
            Configuration.UI_MODE_NIGHT_YES ->{
                title_ll.setBackgroundColor(Color.BLACK)
                btn_close.setTextColor(Color.WHITE)
                btn_share.setTextColor(Color.WHITE)
            }
            // Default
            Configuration.UI_MODE_NIGHT_NO -> {
                title_ll.setBackgroundColor(Color.WHITE)
                btn_close.setTextColor(Color.BLACK)
                btn_share.setTextColor(Color.BLACK)
            }
        }

        // 1. Fragment 어댑터 생성
        val fAdt = FragmentAdapter(supportFragmentManager)
        // 2. 링크 가져와 frament 추가
        bookLink.forEachIndexed { i, s ->
            fAdt.addFragment(BookFragment.newInstance(s, i), s);
        }

        // 3. ViewPager 어댑터 설정 & 화면에 띄울 item(Intent로 전달받은 index로 확인) 설정
        book_vp.adapter = fAdt
        book_vp.currentItem = index

        // 4. X 버튼 클릭
        btn_close.setOnClickListener {
            finish()
        }
        // 5. Url TextView
        // - text 세팅
        book_url.text = bookLink[index]
        // - 클릭 => 링크 복사 세팅
        book_url.setOnLongClickListener { //클립보드 사용 코드
            val clipboardManager: ClipboardManager =
                applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText(
                Constants.BOOK_INFO_URL,
                bookLink[index]
            ); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
            clipboardManager.setPrimaryClip(clipData)

            //복사가 되었다면 토스트메시지 노출
            Toast.makeText(applicationContext, "URL이 복사되었습니다.", Toast.LENGTH_SHORT).show();

            true
        }

        // 6. 공유 버튼
        btn_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)

            // Set default text message
            // 카톡, 이메일, MMS 다 이걸로 설정 가능
            //String subject = "문자의 제목";
            val text = book_url.text.toString()
            //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type = "text/plain"
            // Title of intent
            val chooser = Intent.createChooser(intent, "친구에게 공유하기")
            startActivity(chooser)
        }

        // + 스크롤 에니메이션 동작 설정
        book_vp!!.setDurationScroll(300) // 시간 설정
        book_vp!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                index = position
                // 변경된 링크 Text 적용
                book_url.text = bookLink[index]
                // Url Scroll 맨 앞으로
                url_sv.scrollTo(0, 0)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


//   override fun onStart() {
//        Log.d("LifeCycle_Info", "onStart() 호출!!")
//        super.onStart()
//    }
//
//    override fun onResume() {
//        Log.d("LifeCycle_Info", "onResume() 호출!!")
//        super.onResume()
//    }
//
//    override fun onPause() {
//        Log.d("LifeCycle_Info", "onPause() 호출!!")
//        super.onPause()
//    }
//
//    override fun onStop() {
//        Log.d("LifeCycle_Info", "onStop() 호출!!")
//        super.onStop()
//    }
//
//    override fun onDestroy() {
//        Log.d("LifeCycle_Info", "onDestroy() 호출!!")
//        super.onDestroy()
//    }
}