package com.example.booksearch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import androidx.viewpager.widget.ViewPager
import com.example.booksearch.ui.BookFragment
import com.example.booksearch.ui.adpater.BookViewPager

class BookWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {
    override fun overScrollBy(
        deltaX: Int,
        deltaY: Int,
        scrollX: Int,
        scrollY: Int,
        scrollRangeX: Int,
        scrollRangeY: Int,
        maxOverScrollX: Int,
        maxOverScrollY: Int,
        isTouchEvent: Boolean
    ): Boolean {
        if(parent.parent is BookViewPager) {
            val viewPager: BookViewPager = (parent.parent as BookViewPager)
            if(viewPager.scrollState == ViewPager.SCROLL_STATE_IDLE) {
                if(deltaX > 0) {
                    if(checkAdapterLimits(1, viewPager.currentItem) ) //right
                        (parent.parent as ViewPager).currentItem +=  1
                }
                else if(deltaX < 0) {
                    if(checkAdapterLimits(-1, viewPager.currentItem) ) //left
                        (parent.parent as ViewPager).currentItem -= 1
                }
                else{// 이외에는 상하 스크롤 => super.overScrollBy() 호출!!
                    super.overScrollBy(
                        deltaX,
                        deltaY,
                        scrollX,
                        scrollY,
                        scrollRangeX,
                        scrollRangeY,
                        maxOverScrollX,
                        maxOverScrollY,
                        isTouchEvent
                    )
                }
            }
        }
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)

        return super.onInterceptTouchEvent(event)
    }

    /**
     * @param direction   이동 할 방향(-1: 좌, 1: 우)
     * @param position    현재 페이지 번호
     */
    private fun checkAdapterLimits(direction: Int, position: Int) : Boolean {
        return if(direction < 0) //left
            position >= 1 // 현재 페이지가 0보다 크면 왼쪽으로 간다!! => return true
        else //right
            // 현재 페이지가 최대 페이지 수(-1) 보다 작으면 오른쪽으로 간다!! => return true
            position < (parent.parent as ViewPager).adapter?.count?.minus(1)!!

    }
}