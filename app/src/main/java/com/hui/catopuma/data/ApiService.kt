package com.hui.catopuma.data

import com.hui.catopuma.data.bean.Ad
import com.hui.catopuma.data.bean.Ads
import com.hui.catopuma.data.bean.ResponseBean
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/2611:35
 *    desc   :
 */
interface ApiService {

    /**
     * 通用数据mock
     */
    @GET(Apis.MOCK_DATA)
    suspend fun commonMock(@Path("id") path:String): ResponseBean<Any>

    /**
     * 文件下载
     */
    @Streaming
    @GET
    suspend fun downLoadFile(@Url url: String?):ResponseBody

    /**
     * 启动页广告配置获取
     */
    @GET("home/startup")
    suspend fun launchConfig(): ResponseBean<MutableList<Ad>>

    /**
     * 启动页广告配置获取，Gson解析测试
     */
    @GET(Apis.MOCK_DATA)
    suspend fun launchConfigTest(@Path("id") path:String):ResponseBean<Ads>



}