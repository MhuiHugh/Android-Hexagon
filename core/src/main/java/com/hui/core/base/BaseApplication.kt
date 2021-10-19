package com.hui.core.base

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.hui.core.imageloader.ImageLoaderV4
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import java.util.*
import kotlin.properties.Delegates

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/3 16:10
 *    desc   :
 *  TODO tip 1：需要为项目准备一个 Application 来继承 BaseApplication，
 * 以便在 Activity/Fragment 中享用 Application 级作用域的 Callback-ViewModel
 * callback-ViewModel 的职责仅限于在 "跨页面通信" 的场景下，承担 "唯一可信源"，
 * 所有跨页面的 "状态同步请求" 都交由该可信源在内部决策和处理，并统一分发给所有订阅者页面。
 * 如果这样说还不理解的话，详见《LiveData 鲜为人知的 身世背景 和 独特使命》中结合实际场合 对"唯一可信源"本质的解析。
 * https://xiaozhuanlan.com/topic/0168753249
 */
open class BaseApplication : Application(), ViewModelStoreOwner {

    private var mAppViewModelStore: ViewModelStore? = null

    companion object{
        private var instance: BaseApplication by Delegates.notNull()
        fun instance() = instance
    }

    //--------生命周期方法---------------
    override fun onCreate() {
        super.onCreate()
        initBase()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        CoreLog.v(CoreConfig.TAG, "onLowMemory()")
        ImageLoaderV4.getInstance().clearMemoryCache(this)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore!!
    }

    //--------------------

    //--------------------

    private fun initBase() {
        CoreLog.v(CoreConfig.TAG, "initBase()")
        instance=this
        mAppViewModelStore = ViewModelStore()
        //设置时区为北京
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))

        initX5()
    }

    private fun initX5(){
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)

        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                CoreLog.d( CoreConfig.TAG,"x5内核初始化 onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                CoreLog.d( CoreConfig.TAG,"onCoreInitFinished()")
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb)
    }

    //--------------------


}