package com.hui.catopuma.databind

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.hui.catopuma.R
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.abs.IPagerNavigator

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1715:17
 *    desc   : 指示器 https://github.com/hackware1993/MagicIndicator
 *
 */
object MagicIndicatorDatabindAdapter {

    @JvmStatic
    @BindingAdapter("magicIndicatorNavigator")
    fun magicIndicatorNavigator(view:MagicIndicator?,mnavigator: IPagerNavigator){
        view?.navigator=mnavigator
    }

    /**
     * 指示器和ViewPager关联，需要findView情况
     */
    @JvmStatic
    @BindingAdapter("magicIndicatorViewPager")
    fun magicIndicatorViewPager(view: View?,no:Boolean) {
        val magicIndicator = view?.findViewById<MagicIndicator>(R.id.main_magic_indicator)
        val viewPager = view?.findViewById<ViewPager>(R.id.main_vp)
        if (null != magicIndicator && null != viewPager) {
            ViewPagerHelper.bind(magicIndicator, viewPager)
        }
    }

}