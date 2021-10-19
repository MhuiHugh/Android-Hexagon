package com.hui.core.http

import com.hui.core.http.factory.GabonConverterFactory
import com.hui.core.http.interceptor.CacheInterceptor
import com.hui.core.http.interceptor.RequestInterceptor
import okhttp3.*
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by Petterp
 * on 2020-01-13
 * Function:
 */

object LiveHttp {
    private val mRetrofit: Retrofit
    private val mOkHttpClient: OkHttpClient
    private val mApiMaps: HashMap<String, Any>

    init {
        val builder = OkHttpClient.Builder()
        val liveConfig = LiveConfig.config
        val interceptorList: ArrayList<Interceptor> = liveConfig.mInterceptorList

        if (liveConfig.mIsCache) {
            builder.addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(RequestInterceptor())
                .cache(Cache(liveConfig.mContext.cacheDir, 20 * 1024 * 1024))
        }
        for (interceptor in interceptorList) {
            builder.addInterceptor(interceptor)
        }

        mOkHttpClient = builder.retryOnConnectionFailure(true)
            .connectTimeout(liveConfig.mConnectTimeout, TimeUnit.SECONDS)
            .writeTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .readTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(liveConfig.mBaseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GabonConverterFactory.create(LiveConfig.config.mGson))
            .build()
        mApiMaps = HashMap(10)
    }


    fun <T> createApi(clazz: Class<T>): T {
        val name = clazz.canonicalName
        if (name in mApiMaps) {
            return mApiMaps[name] as T
        }
        val t = mRetrofit.create(clazz)
        if (name != null) {
            mApiMaps += name to t as Any
        }
        return t
    }

    fun <T> createApi(clazz: Class<T>, baseUrl: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GabonConverterFactory.create(LiveConfig.config.mGson))
            .build()
        return retrofit.create(clazz)
    }
}