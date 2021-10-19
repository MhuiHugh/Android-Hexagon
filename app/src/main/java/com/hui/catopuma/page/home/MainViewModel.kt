package com.hui.catopuma.page.home

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import com.hui.catopuma.R
import com.hui.catopuma.adapter.MainViewPagerAdapter
import com.hui.catopuma.data.Constant
import com.hui.catopuma.data.bean.Ad
import com.hui.catopuma.viewmodel.BaseViewModel
import com.hui.core.base.BaseApplication
import com.hui.core.http.error.launchVMHttp
import com.hui.core.http.file.DownloadFileKtx
import com.hui.core.http.file.overSchedule
import com.hui.core.utils.LogUtils
import kotlinx.coroutines.flow.collect
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1714:35
 *    desc   :
 */
class MainViewModel : BaseViewModel() {

    /**
     * 底部tab配置
     */
    var navigatorItems: MutableList<Ad>? = arrayListOf()
    /**
     * 底部Tab指示器
     */
    val commonNavigator: ObservableField<CommonNavigator> = ObservableField()

    /**
     * 指示器Adapter
     */
    lateinit var commonNavigatorAdatper: CommonNavigatorAdapter

    /**
     * ViewPagerAdapter
     */
    val viewPagerAdapter: ObservableField<MainViewPagerAdapter> = ObservableField()

    /**
     * Fragment
     */
    val viewPageFragmentList: MutableList<Fragment>?=arrayListOf()

    var viewPageCurrentItem:ObservableInt=ObservableInt(0)

    init {
        LogUtils.v("${Constant.TAG} MainViewModel init ")
        val ad0 = Ad("", "", BaseApplication.instance().getString(R.string.main_home), 100, R.drawable.test_smail)
        val ad1 = Ad("", "", BaseApplication.instance().getString(R.string.main_me), 100, R.drawable.test_smail)
        navigatorItems?.add(ad0)
        navigatorItems?.add(ad1)

    }

    fun loadStartupAd(){
        //disPlayImageProgress(Context context, String url, ImageView imageView, int placeholderResId, int errorResId, OnGlideImageViewListener listener) {

    }

}