package com.example.fx

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTabHost
import androidx.viewpager.widget.ViewPager
import com.example.fx.fragment.FragmentAdapter
import com.example.fx.fragment.FragmentFirst
import com.example.fx.fragment.FragmentSecond
import com.example.fx.fragment.FragmentThird
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {
    var host : FragmentTabHost?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }
    fun initView(){
        /** Viewpager & adpater */
        // fragment adapter instance 생성
        val mFragmentAdapter = FragmentAdapter(supportFragmentManager)
        // fragment adapter에 fragment 추가(fragment,title)
        mFragmentAdapter.addFragment(FragmentFirst(), "fragment1")
        mFragmentAdapter.addFragment(FragmentSecond(), "fragment2")
        mFragmentAdapter.addFragment(FragmentThird(), "fragment3")
        // viewpager.adapter 초기화
        viewpager.adapter = mFragmentAdapter

        /**
         *  Radio Button Click
         * - click된 radio button의 해당 fragment로 변경
         */
        radio_group1.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.rb1-> viewpager.currentItem = 0
                R.id.rb2-> viewpager.currentItem = 1
                R.id.rb3-> viewpager.currentItem = 2
            }
        }

        /**
         *  ViewPager Selected
         * - Page Scroll 발생 시 해당 Radio Button으로 Check상태 변경
         */
        viewpager.setOnPageChangeListener (object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }
            override fun onPageSelected(position: Int) {
                /** position번째 => check */
                when(position){
                    0->{
                        radio_group1.check(R.id.rb1)
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell)
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy)
                        chartButton.visibility = View.VISIBLE
                    }
                    1->{
                        radio_group1.check(R.id.rb2)
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell_under)
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy_under)
                        chartButton.visibility = View.GONE
                    }
                    2->{
                        radio_group1.check(R.id.rb3)
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell_under)
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy_under)
                        chartButton.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}