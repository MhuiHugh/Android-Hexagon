package com.hui.catopuma.page.startup

import android.content.Intent
import androidx.lifecycle.Observer
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hui.catopuma.R
import com.hui.catopuma.BR
import com.hui.catopuma.adapter.StartupAdapter
import com.hui.catopuma.data.Constant
import com.hui.catopuma.page.home.MainActivity
import com.hui.catopuma.page.home.MainViewModel
import com.hui.catopuma.widget.TimeTextView
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.utils.DataStoreUtils
import com.hui.core.utils.LogUtils
import com.youth.banner.indicator.CircleIndicator

/**
 * 启动页，引导页，广告页
 */
class StartupActivity : BaseActivity() {

    lateinit var mViewModel: StartupViewModel

    lateinit var click: ClickProxy

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(StartupViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        //TODO tip 1: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        //点击监听初始化
        click = ClickProxy()
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        return DataBindingConfig(R.layout.activity_startup, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    /**
     * 状态栏，返回栏设置
     */
    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)//状态栏字体是深色，不写默认为亮色
            .flymeOSStatusBarFontColor(R.color.color_333)//修改flyme OS状态栏字体颜色
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .transparentBar()
            .init()
    }

    override fun initInCreate() {
        if (DataStoreUtils.readBooleanData(Constant.KEY_SHOW_WELCOME, true)) {
            //设置指示器
            mViewModel.bannerIndicator.set(CircleIndicator(this))
            mViewModel.showWelcome()
            //设置点击事件回调
            mViewModel.bannerAdapter.setOnListenerClickWelcome(click)
        } else {
            mViewModel.getLaunchConfig()
        }

        mViewModel.goHome.observeInActivity(this, Observer {
            LogUtils.d("${Constant.TAG} 状态变化：$it")
            if(it){
                if(null!=click){
                    click.clckAd()
                }
            }
        })
    }

    /**
     * 点击事件
     */
    inner class ClickProxy : StartupAdapter.OnListenerClickWelcome,TimeTextView.OnTimeZeroListener {

        /**
         * 引导页点击
         */
        override fun clickWelcome() {
            DataStoreUtils.putSyncData(Constant.KEY_SHOW_WELCOME, false)
            goToHome(false)
        }

        /**
         * 广告倒计时结束
         */
        override fun onTimeZero(view: TimeTextView?) {
            //LogUtils.d(Constant.TAG, "onTimeZero()")
            mViewModel.adTimeStart.set(false)
            goToHome(false)
        }

        /**
         * 跳过广告
         */
        fun clickAdSkip() {
            LogUtils.d("clickAdSkip()")
            //停止倒计时
            mViewModel.adTimeStart.set(false)
            goToHome(false)
        }

        /**
         * 广告点击
         */
        fun clckAd(){
            LogUtils.d("clckAd()")
            mViewModel.adTimeStart.set(false)
            goToHome(true)
        }

        /**
         * 点击测试
         */
        fun clickTest() {
            LogUtils.d("clickTest()")
            mViewModel.getLaunchConfig()
        }
    }

    //------------------

    fun goToHome(loadAd:Boolean=false) {
        val intent=Intent(this,MainActivity::class.java)
        intent.putExtra("loadAd", loadAd)
        if(null!=mViewModel.ad.get()) {
            intent.putExtra("action", mViewModel.ad.get()?.action)
            intent.putExtra("imageUrl", mViewModel.ad.get()?.imageUrl)
        }
        startActivity(intent)
        finishActivity()
    }

}