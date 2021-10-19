package com.hui.catopuma.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1715:16
 *    desc   :
 */
class MainViewPagerAdapter(fragmentManager: FragmentManager,mpages:List<Fragment>):FragmentPagerAdapter(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val pages=mpages

    override fun getCount(): Int {
      return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

}