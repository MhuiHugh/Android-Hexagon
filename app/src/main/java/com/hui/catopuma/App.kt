package com.hui.catopuma

import android.view.Gravity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.PathUtils
import com.hjq.toast.ToastUtils
import com.hui.catopuma.data.Apis
import com.hui.catopuma.data.Config
import com.hui.catopuma.data.Constant
import com.hui.core.base.BaseApplication
import com.hui.core.http.LiveConfig
import com.hui.core.http.error.CodeBean
import com.hui.core.http.error.EnumException
import com.hui.core.http.net.INetEnable
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog
import com.hui.core.utils.DataStoreUtils
import com.hui.core.utils.LogUtils
import com.tencent.bugly.crashreport.CrashReport

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/315:31
 *    desc   :
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        dealInit()
    }

    private fun dealInit() {
        ToastUtils.init(this)
        ToastUtils.setGravity(Gravity.BOTTOM,0, ConvertUtils.dp2px(90.0f))
        DataStoreUtils.init(this)
        initBugyly()
        initLiveHttp()
    }

    private fun initBugyly() {
        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_ID, BuildConfig.DEBUG)
    }

    private fun initLiveHttp(){
        //  /data/user/0/com.hui.catopuma/cache
        val path=PathUtils.getInternalAppCachePath()

        LiveConfig
            .context(this)
            .baseUrl(Apis.MOCK_BASE)   //baseUrl
            .writeTimeout(CoreConfig.HTTP_READ_TIME)     //设置读写超时时间，默认30l
            .connectTimeout(CoreConfig.HTTP_CONNECT_TIME)  //设置连接超时时间,默认10l
            .isCache(true)     //开启网络缓存，默认关闭
            .filePath(path)    //文件下载路径
            .log()   //需要导入 logger 依赖
            .initNetObser() //监听网络,必须在context之后
            .initNetObser(object : INetEnable {
                override fun netOpen() {
                    //网络打开
                }

                override fun netOff() {
                    //网络关闭
                }
            })
            //以下错误逻用于全局请求码处理,具体处理域皆处于挂起函数
            .errorCodeKtx(101, CodeBean {
                //默认为发起网络请求的线程，一般为io,记得调用withContext()
                //对于code=101的处理
            })
            //通用网络处理方案
            .universalErrorHttpKtx {
                //EnumException.CONNECT_EXCEPTION
                //EnumException.TIMEOUT_EXCEPTION
                //EnumException.SOCKET_EXCEPTION,
                //EnumException.NET_UNAVAILABLE,
                CoreLog.e(CoreConfig.TAG,"通用网络连接-超时等失败，如网络开启，但网络其实不可用等情况")
            }
            //以下错误用于指定网络异常处理，具体处理域皆处于挂起函数
            .errorHttpKtx(EnumException.NET_DISCONNECT) {
                CoreLog.e(CoreConfig.TAG,"网络断开")
            }
    }

}