package com.example.uip

import android.content.res.Configuration
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TabHost
import androidx.appcompat.app.AppCompatActivity
import com.example.uip.Fragment.FragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    /** Viewpager & adpater */
    var mFragmentAdapter = FragmentAdapter(supportFragmentManager)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setupViewPager()
//        var th = findViewById<TabHost>(R.id.tabHost)
//        th.setup();
//
//        var b = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//
//        if(b){
//            val tw = tabHost.tabWidget
//            tw.orientation = LinearLayout.VERTICAL
//        }
//
//        th.addTab(tabHost.newTabSpec("tab name").setIndicator("title").setContent(R.layout.fragment_first))
//        th.addTab(tabHost.newTabSpec("tab name").setIndicator("title").setContent(R.layout.fragment_second))
//        th.addTab(tabHost.newTabSpec("tab name").setIndicator("title").setContent(R.layout.fragment_third))
//
//
//

    }


//    fun setupViewPager(){
//        mFragmentAdapter.addFragment(FirstFragment(), "First")
//        mFragmentAdapter.addFragment(SecondFragment(), "Second")
//        mFragmentAdapter.addFragment(ThirdFragment(), "Third")
//        container.adapter = mFragmentAdapter
//        tabs.setupWithViewPager(container)
//    }
}