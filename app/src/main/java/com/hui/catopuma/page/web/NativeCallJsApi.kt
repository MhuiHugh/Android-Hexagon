package com.hui.catopuma.page.web

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/5/10 15:06
 *    desc   :原生调用js
 */
open abstract interface NativeCallJsApi {

    /**
     * 原生调用js 返回
     */
    abstract fun callJsBack(method:String,success:Boolean)

}