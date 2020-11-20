package com.example.booksearch.ui.adpater

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class FragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()   // fragment
    private val mFragmentLinkList: MutableList<String> = ArrayList() // book link

    /**
     * - Activity에서 Fragment(화면)를 추가할 때 호출할 메서드
     * @param fragment    웹뷰 화면
     * @param link        상세 link
     */
    fun addFragment(fragment: Fragment, link: String) {
        mFragmentList.add(fragment)
        mFragmentLinkList.add(link)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}