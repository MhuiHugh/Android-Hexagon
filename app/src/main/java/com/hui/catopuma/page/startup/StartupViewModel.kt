package com.hui.catopuma.page.startup

import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.EncryptUtils
import com.google.gson.internal.LinkedTreeMap
import com.hui.catopuma.R
import com.hui.catopuma.adapter.StartupAdapter
import com.hui.catopuma.data.Constant
import com.hui.catopuma.data.bean.Ad
import com.hui.catopuma.data.bean.asyncBlockMain
import com.hui.catopuma.viewmodel.BaseViewModel
import com.hui.catopuma.widget.TimeTextView
import com.hui.core.base.BaseApplication
import com.hui.core.http.error.launchVMHttp
import com.hui.core.imageloader.ImageLoaderV4
import com.hui.core.imageloader.listener.IImageLoaderListener
import com.hui.core.utils.DataStoreUtils
import com.hui.core.utils.LogUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/317:21
 *    desc   :
 */
class StartupViewModel : BaseViewModel(), IImageLoaderListener {

    /**
     * 是否显示广告页
     */
    val showAd = ObservableBoolean()

    val adTimeStart = ObservableBoolean()

    /**
     * 启动页广告
     */
    val ad: ObservableField<Ad> = ObservableField<Ad>()

    /**
     * 时间格式化
     */
    val adTimePrecision: TimeTextView.TimePrecision = TimeTextView.TimePrecision.MILLISECOND_200
    val adStrId = R.string.startup_ad

    /**
     * vm和Activity交互,默认进入首页
     */
    val goHome: UnPeekLiveData<Boolean> = UnPeekLiveData<Boolean>()

    //--------------------------------------

    /**
     * 是否显示引导页
     */
    val showWelcome = ObservableBoolean()

    /**
     * 引导页
     */
    var bannerAdapter: StartupAdapter = StartupAdapter(null)

    /**
     * 指示器
     */
    var bannerIndicator: ObservableField<CircleIndicator> = ObservableField()

    /**
     * 引导页数据
     */
    var bannerAds: MutableList<Ad>? = arrayListOf()

    init {
        goHome.value=false
    }

    //--------------------------------------

    /**
     * 图片加载失败
     */
    override fun onLoadingFailed(url: String?, target: ImageView?, exception: Exception?) {
        showAd.set(false)
    }

    /**
     * 图片加载成功
     */
    override fun onLoadingComplete(url: String?, target: ImageView?) {
        adTimeStart.set(true)
    }

    //--------------------------------------
    /**
     * 获取启动页广告配置
     */
    public fun getLaunchConfig() {
        launchVMHttp {
           val startup= requestHttp.commonMock("1876683")
           LogUtils.d("${Constant.TAG} ${startup.success}  ${(Constant.HTTP_SUCCESS==startup.code)} $startup")
           if(startup.success){
               //测试mock
               var tempAd: Ad? = null
               val result =startup.result as? LinkedTreeMap<String, Ad>
               if (null != result) {
                   val ad0 =  result.get("ad") as? LinkedTreeMap<String, String>
                   if (null != ad0) {
                       tempAd = Ad(
                           ad0["action"] ?: "",
                           ad0["imageUrl"] ?: "",
                           ad0["title"] ?: "",
                           100, 0
                       )
                   }
                   ad.set(tempAd)
               }

               val savedMd5 = DataStoreUtils.readStringData(Constant.KEY_STARTUP_AD, "")
               val md5 = EncryptUtils.encryptMD5ToString("${tempAd?.action}${tempAd?.imageUrl}")

               //同步方式判断是否有缓存
               val hasCache = ImageLoaderV4.getInstance().hasImageCacheUrlSync(
                   BaseApplication.instance().applicationContext,
                   tempAd?.imageUrl
               )
               //LogUtils.d("${Constant.TAG} 广告图缓存: $hasCache $savedMd5  缓存： $md5 ${ad.get()?.imageUrl}")
               if ((savedMd5 == md5) && hasCache) {
                   //广告图有缓存
                   withContext(Dispatchers.Main) {
                       showAd.set(true)
                   }
               } else {
                   //到首页
                   withContext(Dispatchers.Main) {
                       goHome.value= true
                   }
               }
           }else{
               withContext(Dispatchers.Main) {
                   showAd.set(false)
               }
           }
        }
    }

    /**
     * 结果切回Main线程
     */
    public fun getLaunchConfig1() {
        launchVMHttp {
            requestHttp.launchConfigTest("1876683").asyncBlockMain(onSuccess = {
              //LogUtils.d("${Constant.TAG} ${it}")
            },onError = {

            })
        }
    }

    /**
     * 显示引导页
     */
    fun showWelcome() {
        val banner0 = Ad("", "", "", 100, R.drawable.startup_guide_1)
        val banner1 = Ad("", "", "", 100, R.drawable.startup_guide_2)
        val banner2 = Ad("", "", "", 101, R.drawable.startup_guide_3)

        bannerAds?.add(banner0)
        bannerAds?.add(banner1)
        bannerAds?.add(banner2)

        bannerAdapter.setDatas(bannerAds)

        showAd.set(false)
        showWelcome.set(true)
    }

}