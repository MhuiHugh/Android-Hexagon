package com.hui.core.http.net

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import java.io.IOException

/**
 * @Author petterp
 * @Date 2020/6/11-6:45 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 网络监测
 */
object NetObserver {

    internal var isNetEnable = false
    private var isNetAvailable = false
    internal var iNetEnable: INetEnable? = null
    private var isInit = false

    internal fun init(context: Context) {
        if (isInit) return
        isInit = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getSystemService(ConnectivityManager::class.java) ?: null
            } else {
                context.getSystemService(Context.CONNECTIVITY_SERVICE)?.let {
                    it as ConnectivityManager
                }
            }
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager?.registerNetworkCallback(networkRequest, object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {//有可用网络
                    super.onAvailable(network)
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    networkCapabilities?.let {
                        isNetEnable = true
                        iNetEnable?.netOpen()
                        //获取网络类型
//                        val isWIFI = it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//                        val isMobile = it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    isNetEnable = false
                    iNetEnable?.netOff()
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    isNetEnable = false
                    iNetEnable?.netOff()
                }
            })
        } else {
            //Android N以下通过广播监听
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(NetworkStateReceiver(), intentFilter)
        }
    }

    /** 初始化监听器 */
    fun initNetEnableListener(iNetEnable: INetEnable) {
        this.iNetEnable = iNetEnable
    }

    /** 网络是否关闭 */
    fun isNetEnable(): Boolean = isNetEnable

    /** 判断网络是否可用 */
    fun isAvailable(): Boolean {
        //如果status==0则表示网络可用，其中参数-c 1是指ping的次数为1次，-w是指超时时间单位为s
        try {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 100 www.baidu.com")
            val status = process.waitFor()
            isNetAvailable = status == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            isNetAvailable = false
            return isNetAvailable
        }
    }
}

