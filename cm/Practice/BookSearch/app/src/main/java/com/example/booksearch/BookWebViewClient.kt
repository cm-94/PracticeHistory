package com.example.booksearch

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

open class BookWebViewClient : WebViewClient() {
    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        view.visibility = View.GONE
        super.onPageStarted(view, url, favicon)
    }
    override fun onPageFinished(view: WebView, url: String?) {
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return false // true => 웹뷰에 아무것도 나오지 않음(실행x)
    }
}