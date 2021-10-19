package com.hui.catopuma.databind

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hui.catopuma.adapter.StartupAdapter
import com.hui.catopuma.data.bean.Ad
import com.youth.banner.Banner
import com.youth.banner.indicator.BaseIndicator

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1115:51
 *    desc   : Banner,ViewPager,Progress
 */
object BannerDatabindAdapter {

    /**
     * 轮播adapter
     */
    @JvmStatic
    @BindingAdapter(value = ["bannerAdapter"],requireAll = false)
    fun bannerAdapter(banner: Banner<MutableList<Ad>, StartupAdapter>, adapter: StartupAdapter?){
        if(null!=adapter) {
            banner.adapter = adapter
        }
    }

    /**
     * 轮播adapter 数据
     */
    @JvmStatic
    @BindingAdapter(value = ["bannerData"],requireAll = false)
    fun bannerData(banner: Banner<MutableList<Ad>, StartupAdapter>, data:MutableList<Ad>){
        banner.adapter.setDatas(data)
    }

    /**
     * 轮播 indicator
     */
    @JvmStatic
    @BindingAdapter(value = ["bannerIndicator"],requireAll = false)
    fun bannerIndicator(banner: Banner<MutableList<Ad>, StartupAdapter>, mIndicator: BaseIndicator?){
        if(null!=mIndicator) {
            banner.indicator = mIndicator
        }
    }

    //---------ViewPager -------------------

    /**
     * ViewPager Adapter
     */
    @JvmStatic
    @BindingAdapter(value = ["viewPagerAdapter"],requireAll = false)
    fun viewPagerAdapter(view: ViewPager, adapter: PagerAdapter){
        if(null!=view&&null!=adapter){
            view.adapter=adapter
            view.setCurrentItem(0,true)
        }
    }

    /**
     * ViewPager offscreenPageLimit
     */
    @JvmStatic
    @BindingAdapter(value = ["viewPagerOffscreenPageLimit"],requireAll = false)
    fun viewPagerOffscreenPageLimit(view: ViewPager, offscreenPageLimit: Int=3){
        if(null!=view){
            view.offscreenPageLimit=offscreenPageLimit
        }
    }

    /**
     * ViewPager
     */
    @JvmStatic
    @BindingAdapter(value = ["viewPagerCurrentItem"],requireAll = false)
    fun viewPagerCurrentItem(view: ViewPager, currentItem: Int=0){
        if(null!=view){
            view.setCurrentItem(currentItem,true)
        }
    }

}