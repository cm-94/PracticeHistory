package com.example.fx

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
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

        // TODO 통신 연결

        // TODO 데이터 세팅(NewOrder Object)
        setNewOrderData()

        // TODO View 세팅
        initView()


    }


    fun setNewOrderData(){

        NewOrder.callAmount = 1000
    }

    /**
     * - activity_main의 각 View들을 초기화 한다
     * - NewOrder object property 사용
     * TODO 사전에 데이터를 수신해 NewOrder를 초기화 해주는 작업이 필요하다
     */
    private fun initView(){
        /** callprice,sellprice => call_price,sell_price */
        val callStr = SpannableStringBuilder(NewOrder.callprice.toString())
        callStr.setSpan(AbsoluteSizeSpan(250),4,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        call_price.text = callStr

        val sellStr = SpannableStringBuilder(NewOrder.sellprice.toString())
        sellStr.setSpan(AbsoluteSizeSpan(250),4,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sell_price.text = sellStr

        high_price.text = NewOrder.maxprice.toString()
        low_price.text = NewOrder.minprice.toString()
        fluctate.text = NewOrder.fluctate.toString()

        val spreadStr = SpannableStringBuilder(resources.getString(R.string.spread,NewOrder.spread))
        spreadStr.setSpan(AbsoluteSizeSpan(80),5,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spread_text.text = spreadStr

//        spread_text.text = resources.getString(R.string.spread,NewOrder.spread)


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
                        radio_group1.check(R.id.rb1)                                /** Radio button check 변경 */
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell)  /** Order(sell) 배경 변경 */
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy)   /** Order(call) 배경 변경 */

                        sellText.setTextColor(Color.WHITE)                           /** Order(sell) Text색 변경 */
                        sell_price.setTextColor(Color.WHITE)
                        callText.setTextColor(Color.WHITE)                          /** Order(call) Text색 변경 */
                        call_price.setTextColor(Color.WHITE)

                        chartButton.visibility = View.VISIBLE                       /** chart 버튼 안보이게 */
                    }
                    1->{
                        radio_group1.check(R.id.rb2)
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell_under)
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy_under)

                        sellText.setTextColor(Color.BLUE)
                        sell_price.setTextColor(Color.BLUE)
                        callText.setTextColor(Color.RED)
                        call_price.setTextColor(Color.RED)

                        chartButton.visibility = View.GONE
                    }
                    2->{
                        radio_group1.check(R.id.rb3)
                        sellLayout.setBackgroundResource(R.drawable.ic_order_sell_under)
                        callLayout.setBackgroundResource(R.drawable.ic_order_buy_under)

                        sellText.setTextColor(Color.BLUE)
                        sell_price.setTextColor(Color.BLUE)
                        callText.setTextColor(Color.RED)
                        call_price.setTextColor(Color.RED)

                        chartButton.visibility = View.GONE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}