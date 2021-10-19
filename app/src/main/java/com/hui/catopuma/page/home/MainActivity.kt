package com.hui.catopuma.page.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.TimeUtils
import com.hjq.toast.ToastUtils
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.adapter.MainViewPagerAdapter
import com.hui.catopuma.data.Constant
import com.hui.catopuma.data.bean.Ad
import com.hui.catopuma.page.web.WebActivity
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.imageloader.ImageLoaderV4
import com.hui.core.imageloader.listener.IImageLoaderListener
import com.hui.core.utils.DataStoreUtils
import com.hui.core.utils.LogUtils
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView
import java.lang.Exception

/**
 * 首页
 */
class MainActivity : BaseActivity() {

    lateinit var mViewModel: MainViewModel
    lateinit var click: MainActivity.ClickProxy

    /**
     * 再按一次退出程序
     */
    var lastBackTime:Long=0

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(MainViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        //点击监听初始化
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_main, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        LogUtils.v("${Constant.TAG} initInCreate()")
        mViewModel.commonNavigatorAdatper = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mViewModel.navigatorItems?.size?:0
            }
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val commonPagerTitleView = CommonPagerTitleView(context)
                val ad: Ad? = mViewModel?.navigatorItems?.get(index)
                    val customLayout: View = LayoutInflater.from(context).inflate(
                        R.layout.item_bottom_tab,
                        null
                    )
                    val titleImg: ImageView =
                        customLayout.findViewById<View>(R.id.title_img) as ImageView
                    val titleText = customLayout.findViewById<View>(R.id.title_text) as TextView
                    if (ad?.imageUrl.isNullOrEmpty()) {
                        ImageLoaderV4.getInstance().displayImageInResource(this@MainActivity, ad?.imageDrawableId?:0, titleImg)
                    } else {
                        ImageLoaderV4.getInstance().displayImage(this@MainActivity, ad?.imageUrl, titleImg)
                    }
                    titleText.text = ad?.title
                    commonPagerTitleView.setContentView(customLayout)

                commonPagerTitleView.onPagerTitleChangeListener =
                    object : CommonPagerTitleView.OnPagerTitleChangeListener {
                        override fun onSelected(index: Int, totalCount: Int) {
                            titleText.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_333))
                        }

                        override fun onDeselected(index: Int, totalCount: Int) {
                            titleText.setTextColor(ContextCompat.getColor(applicationContext,R.color.color_999))
                        }

                        override fun onLeave(
                            index: Int,
                            totalCount: Int,
                            leavePercent: Float,
                            leftToRight: Boolean
                        ) {
                            titleImg.scaleX = 1.3f + (0.8f - 1.3f) * leavePercent;
                            titleImg.scaleY = 1.3f + (0.8f - 1.3f) * leavePercent;
                        }

                        override fun onEnter(
                            index: Int,
                            totalCount: Int,
                            enterPercent: Float,
                            leftToRight: Boolean
                        ) {
                            titleImg.scaleX = 0.8f + (1.3f - 0.8f) * enterPercent;
                            titleImg.scaleY = 0.8f + (1.3f - 0.8f) * enterPercent;
                        }
                    }

                commonPagerTitleView.setOnClickListener {
                    mViewModel?.viewPageCurrentItem.set(index)
                }
                return commonPagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
              return null
            }
        }

        val navigator=CommonNavigator(this)
        navigator.isAdjustMode=true
        navigator.adapter=mViewModel.commonNavigatorAdatper
        mViewModel?.commonNavigator.set(navigator)

        //ViewPager Fragment
        val homeFragment= HomeFragment()
        val personalFragment= PersonalFragment()

        mViewModel.viewPageFragmentList?.clear()
        mViewModel.viewPageFragmentList?.add(homeFragment)
        mViewModel.viewPageFragmentList?.add(personalFragment)

        val mainViewPagerAdapter=MainViewPagerAdapter(supportFragmentManager,mViewModel.viewPageFragmentList!!)
        mViewModel.viewPagerAdapter.set(mainViewPagerAdapter)
        mViewModel.viewPagerAdapter.notifyChange()

        //启动页广告图缓存
        var loadAd=false
        var imageUrl:String?=""
        var action:String?=""

        if (intent.hasExtra("loadAd")) {
            loadAd = intent.getBooleanExtra("loadAd", false)
            if (intent.hasExtra("imageUrl")) {
                action = intent.getStringExtra("action")
                imageUrl = intent.getStringExtra("imageUrl")

                //启动页广告图缓存
                val savedMd5 = DataStoreUtils.readStringData(Constant.KEY_STARTUP_AD, "")
                val md5 = EncryptUtils.encryptMD5ToString("$action$imageUrl")
                LogUtils.d("$savedMd5  $md5")
                if (savedMd5 != md5) {
                    ImageLoaderV4.getInstance()
                        .downLoadImage(this, imageUrl, object : IImageLoaderListener {
                            override fun onLoadingFailed(
                                url: String?,
                                target: ImageView?,
                                exception: Exception?
                            ) {
                                //LogUtils.d("onLoadingFailed() $url")
                            }

                            override fun onLoadingComplete(url: String?, target: ImageView?) {
                                //LogUtils.d("onLoadingComplete() $url")
                                if (url == imageUrl) {
                                    DataStoreUtils.putSyncData(Constant.KEY_STARTUP_AD, md5)
                                }
                            }
                        })
                }
            }
        }

        //广告点击跳转
        if (loadAd&&!action.isNullOrEmpty()) {
            WebActivity.startWebActivity(this, action)
        }
    }

    //-------------------------------------

    override fun onBackPressed() {
        LogUtils.d("onBackPressed()")
        val nowTime=TimeUtils.getNowMills()
        if(nowTime-lastBackTime>3000){
            lastBackTime=nowTime
            ToastUtils.show(getString(R.string.main_exit))
        }else{
            finishAllActivity()
        }
    }

    //-------------------------------------

    /**
     * 点击事件
     */
    inner class ClickProxy {

        fun clickTest(view: View?) {

        }

    }

    //--------------------------------


}