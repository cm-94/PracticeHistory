package com.example.fcex

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TabHost
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTabHost
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {
    var host : FragmentTabHost?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        host = findViewById(R.id.tabHost)
        tabHost.setup(this,supportFragmentManager,R.id.content)

        var tabSpec1 = tabHost.newTabSpec("tab1"); // 구분자
        tabSpec1.setIndicator("1"); // 탭 이름
        tabHost?.addTab(tabSpec1, FragmentFirst::class.java, null);

        var tabSpec2 = tabHost.newTabSpec("tab2");
        tabSpec2.setIndicator("2");
        tabHost.addTab(tabSpec2, FragmentSecond::class.java, null);

        tabHost.getTabWidget().getChildAt(0)
                .setBackgroundColor(Color.parseColor("#3C989E"));
        tabHost.getTabWidget().getChildAt(1)
                .setBackgroundColor(Color.parseColor("#5DB5A4"));


//        tabHost.setCurrentTab(0);

//        tabHost.addTab(tabHost
//            .newTabSpec("first")
//            .setIndicator("First")
//            .setContent(Intent(this  ,FragmentFirst::class.java)))
//        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("Second").setContent(Intent(this  ,FragmentSecond::class.java)))
    }
}