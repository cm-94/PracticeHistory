package com.example.booksearch

import android.webkit.WebView
import android.webkit.WebViewClient

class BookWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return false // true => 웹뷰에 아무것도 나오지 않음(실행x)
    }
}