package com.hui.catopuma.viewmodel

import androidx.lifecycle.ViewModel
import com.hui.catopuma.data.ApiService
import com.hui.core.http.LiveHttp

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/2614:10
 *    desc   :
 */
open class BaseViewModel :ViewModel(){

    val requestHttp= LiveHttp.createApi(ApiService::class.java)
    
}