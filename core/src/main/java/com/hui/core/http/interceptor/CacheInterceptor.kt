package com.hui.core.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 缓存拦截器
 */
class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request()).newBuilder()
            .request(chain.request())
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=" + 60)
            .build()
    }
}
