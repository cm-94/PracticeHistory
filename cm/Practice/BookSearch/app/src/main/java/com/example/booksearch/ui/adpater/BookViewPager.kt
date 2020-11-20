package com.example.booksearch.ui.adpater

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

class BookViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
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
        // 화면 스크롤 지속 시간(ms)
        private var durationScrollMillis = 1
        init {
            this.durationScrollMillis = durationScroll
        }
        // 스크롤 동작
        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis)
        }
    }

}