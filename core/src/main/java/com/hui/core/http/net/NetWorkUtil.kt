package com.hui.core.http.net

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * 判断wifi网络是否可用
 *
 * @param context
 * @return
 */
fun isWifiConnected(context: Context?): Boolean {
    if (context != null) {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWiFiNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable
        }
    }
    return false
}

/**
 * 判断移动网络是否可用
 *
 * @param context
 * @return
 */
fun isMobileConnected(context: Context?): Boolean {
    if (context != null) {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
    }
    return false
}

/**
 * 判断网络连接是否可用
 *
 * @param context
 * @return
 */
fun isNetworkConnected(context: Context?): Boolean {
    if (context != null) {
        val mConnectivityManager = (context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
    }
    return false
}


/**
 * 获取当前网络连接的类型信息
 *
 * @param context
 * @return
 */
fun getConnectedType(context: Context?): Int {
    if (context != null) {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
            return mNetworkInfo.type
        }
    }
    return -1
}

/**
 * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
 *
 * @return
 */
fun ping(): Boolean {
    var result: String? = null
    try {
        val ip = "www.baidu.com" // ping 的地址，可以换成任何一种可靠的外网
        val p =
            Runtime.getRuntime().exec("ping -c 3 -w 100 $ip") // ping网址3次
        // 读取ping的内容，可以不加
        val input = p.inputStream
        val `in` =
            BufferedReader(InputStreamReader(input))
        val stringBuffer = StringBuffer()
        var content: String?
        while (`in`.readLine().also { content = it } != null) {
            stringBuffer.append(content)
        }
        Log.d("------ping-----", "result content : $stringBuffer")
        // ping的状态
        val status = p.waitFor()
        if (status == 0) {
            result = "success"
            return true
        } else {
            result = "failed"
        }
    } catch (e: IOException) {
        result = "IOException"
    } catch (e: InterruptedException) {
        result = "InterruptedException"
    } finally {
        Log.d("----result---", "result = $result")
    }
    return false
}
