package com.example.booksearch.ui

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
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
        // TODO : 뒤로가기(backpress) 이벤트 처리
        val url = intent.getStringExtra(CommonUtils.BOOK_INFO_URL) ?: ""

        // 자바스크립트가 동작할 수 있도록 세팅
        val webSetting = webview.settings
        webSetting.javaScriptEnabled = true
        // 웹뷰 클라이언트 설정
       webview.webViewClient = MyWebViewClient()
        // 웹뷰 실행
        webview.loadUrl(url)
    }


    // WebViewClient 클래스 정의
    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false // true => 웹뷰에 아무것도 나오지 않음(실행x)
        }
    }
}