package com.hui.core.http.interceptor

import com.hui.core.http.LiveConfig
import com.hui.core.http.net.isNetworkConnected
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Petterp
 * on 2020-01-13
 * Function: 缓存
 */
class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (isNetworkConnected(LiveConfig.config.mContext)) {
            val maxAge = 60 //缓存失效时间，单位为秒
            return response.newBuilder()
                .removeHeader("Pragma") //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .header("Cache-Control", "max-age=$maxAge")
                .build()
        }
        return response
    }

}