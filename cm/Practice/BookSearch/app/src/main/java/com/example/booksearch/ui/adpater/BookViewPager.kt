package com.example.booksearch.ui.adpater

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class BookViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var isPagingEnabled : Boolean = true

    var scrollState: Int = SCROLL_STATE_IDLE


    // 좌우 스크롤 에니메이션 동작 시간 설정
    fun setDurationScroll(millis: Int) {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, OwnScroller(context, millis))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 스크롤 동작 클래스
     * @param context
     * @param durationScroll   스크롤 동작 시간
     */
    inner class OwnScroller(context: Context, durationScroll: Int) : Scroller(context, DecelerateInterpolator()) {

        private var durationScrollMillis = 1

        init {
            this.durationScrollMillis = durationScroll
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis)
        }
    }


    override fun overScrollBy(deltaX: Int, deltaY: Int, scrollX: Int, scrollY: Int, scrollRangeX: Int, scrollRangeY: Int, maxOverScrollX: Int, maxOverScrollY: Int, isTouchEvent: Boolean): Boolean {
        if(parent.parent is BookViewPager) {
            val viewPager: BookViewPager = (parent.parent as BookViewPager)
            if(viewPager.scrollState == SCROLL_STATE_IDLE) { // 스크롤 하지 않는 상태
                if(deltaX > 0) {
                    if(checkAdapterLimits(1, viewPager.currentItem)) //right
                        (parent.parent as ViewPager).setCurrentItem(viewPager.currentItem + 1,true)
                }
                else {
                    if(checkAdapterLimits(-1, viewPager.currentItem)) //left
                        (parent.parent as ViewPager).setCurrentItem(viewPager.currentItem - 1,true)
                }
            }
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return this.isPagingEnabled && super.onInterceptTouchEvent(ev)
        }catch (exception : IllegalArgumentException){
            exception.printStackTrace()
        }
        return false
    }

//    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
//        parent.requestDisallowInterceptTouchEvent(true)
//
//        return super.onInterceptTouchEvent(event)
//    }

    private fun checkAdapterLimits(direction: Int, position: Int) : Boolean {
        return if(direction < 0) //left
            position >= 1
        else //right
            position < (parent.parent as ViewPager).adapter!!.count - 1

    }

}