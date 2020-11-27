package com.example.booksearch

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

class BookWebViewClient() : WebViewClient() {
    private lateinit var mContext : Context
    private lateinit var mRootView : View
    private lateinit var progressbar : ProgressBar
    constructor(view: View):this(){
        mRootView = view
        progressbar = mRootView.findViewById(R.id.progressBar_info)
    }

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        progressbar.visibility = View.VISIBLE
        view.visibility = View.GONE
        super.onPageStarted(view, url, favicon)
    }
    override fun onPageFinished(view: WebView, url: String?) {
        progressbar.visibility = View.GONE
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }


    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return false // true => 웹뷰에 아무것도 나오지 않음(실행x)
    }
}