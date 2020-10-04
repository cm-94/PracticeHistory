package com.example.bithumb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.bithumb.Fragment.FragmentManager
import com.example.bithumb.Fragment.SecondFragment
import com.example.bithumb.Fragment.ThirdFragment
import com.example.bithumb.Fragment.TickerFragment
import com.example.bithumb.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /** Viewpager & adpater */
    private lateinit var mViewPager :ViewPager
    var adapter = FragmentManager(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** container = ViewPager */
        setupViewPager(container)
        /** tabs = TapLayout */
        tabs.setupWithViewPager(container)
    }

    /** Tab( fragment ) setting */
    fun setupViewPager(viewPager: ViewPager){
        adapter.addFragment(TickerFragment(), "현재가");
        adapter.addFragment(SecondFragment(), "거래 현황");
        adapter.addFragment(ThirdFragment(), "etc");
        viewPager.setAdapter(adapter);
    }
}