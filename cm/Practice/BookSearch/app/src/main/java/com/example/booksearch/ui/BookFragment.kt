package com.example.booksearch.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.booksearch.BookWebViewClient
import com.example.booksearch.R
import kotlinx.android.synthetic.main.fragment_book.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookFragment : Fragment() {
    private var param_link: String? = null
    private var param_index: Int? = null

    private var bookLink : String = ""
    private var bookIndex : Int = 0


    private lateinit var progressBar_info : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param_link = it.getString(ARG_PARAM1)
            param_index = it.getInt(ARG_PARAM2)

            bookLink = param_link.toString()
            bookIndex = param_index as Int
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mRootView = inflater.inflate(R.layout.fragment_book, container, false)
        progressBar_info = mRootView.findViewById(R.id.progressBar_info)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 자바스크립트가 동작할 수 있도록 세팅
        val webSetting = frg_webview.settings
        webSetting.javaScriptEnabled = true

        // 웹뷰 클라이언트 설정 => 안그럼 브라우저로 강제 실행됨(브라우저 > 웹뷰)
        frg_webview.webViewClient = object : BookWebViewClient(){
            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                progressBar_info.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String?) {
                progressBar_info.visibility = View.GONE
                super.onPageFinished(view, url)
            }


//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                // Update the progress bar with page loading progress
//                progressBar_info.progress = newProgress
//                if (newProgress == 100) {
//                    // Hide the progressbar
//                    progressBar_info.visibility = View.GONE
//                }
//            }
        }
        frg_webview.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar_info.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }
        }
        // 웹뷰 - url 로드
        frg_webview.loadUrl(bookLink)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}