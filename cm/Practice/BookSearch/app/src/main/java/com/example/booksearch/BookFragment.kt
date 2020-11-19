package com.example.booksearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    // TODO: Rename and change types of parameters
    private var param_link: String? = null
    private var param_index: Int? = null

    private var bookLink : String = ""
    private var bookIndex : Int = 0

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
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 자바스크립트가 동작할 수 있도록 세팅
        val webSetting = frg_webview.settings
        webSetting.javaScriptEnabled = true
        // 웹뷰 클라이언트 설정
        frg_webview.webViewClient = BookWebViewClient()
        // 웹뷰 실행
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
        // TODO: Rename and change types and number of parameters
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