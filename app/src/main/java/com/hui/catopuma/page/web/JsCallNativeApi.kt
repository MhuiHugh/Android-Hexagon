package com.hui.catopuma.page.web

import android.webkit.JavascriptInterface

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/17 16:16
 *    desc   :js调原生
 */
open abstract interface JsCallNativeApi {

    abstract fun getActivity(): WebActivity

    abstract fun getClick():WebActivity.ClickProxy

    /**
     * 退出Activity
     */
    @JavascriptInterface
    fun exitWebActivity(){
       getActivity().finishActivity()
    }

}