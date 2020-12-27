package com.example.test

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity() {
    var main_scroll_view : View? = null
    var header_view : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
    }



    fun setupViewPager(){
        main_scroll_view = findViewById<NewScrollView>(R.id.main_scroll_view)
        header_view = findViewById<TabLayout>(R.id.header_view)

        (main_scroll_view as NewScrollView?)?.setHeader(header_view)
    }
}