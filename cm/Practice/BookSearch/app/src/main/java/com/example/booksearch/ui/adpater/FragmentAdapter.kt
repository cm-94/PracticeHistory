package com.example.booksearch.ui.adpater

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class FragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()   // fragment
    private val mFragmentLinkList: MutableList<String> = ArrayList()// fragment title

    /**
     * - Activity에서 Fragment를 추가할 때 호출할 메서드
     * - mFragmentList에 Fragment 추가.
     * - mFragmentTitleList에 title 추가
     * @param fragment( Fragment() ),title( String )
     * @return Unit
     */
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentLinkList.add(title)
    }

    /**
     * 필수 override method
     * getItem : return fragment[i]
     * getCount : return fragment.size
     */
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }



    /**
     * 각 fragment에 argument를 전달한다
     */
    fun setFragmentBundle(bundle: Bundle) {
        mFragmentList.forEach {
            it.arguments = bundle
        }
    }

    /**
     * position번째 fragment를 바꾼다
     */
    fun replaceFragment(position: Int, fragment: Fragment) {
        mFragmentList[position] = fragment
    }

}