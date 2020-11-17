package com.example.booksearch.ui

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.booksearch.R
import com.example.booksearch.util.CommonUtils
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // TODO : 웹뷰 클릭 이벤트 처리
        // TODO : 화면 길이..(naver 가면 길이 넘 길어짐..)
        // TODO :
        val url = intent.getStringExtra(CommonUtils.BOOK_INFO_URL) ?: ""


        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }
//        webview.loadUrl("https://www.google.co.in/")
        webview.loadUrl(url)
    }
}