package com.hui.catopuma.databind

import androidx.databinding.BindingAdapter
import com.hui.catopuma.page.web.JsCallNativeApi
import com.hui.catopuma.page.web.NativeCallJsApi
import com.hui.core.widget.dsbridge.DWebView
import com.hui.core.widget.dsbridge.OnReturnValue




/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1214:59
 *    desc   : WebView
 *    https://github.com/wendux/DSBridge-Android/blob/master/readme-chs.md
 */
object WebViewDatebindAdapter {

    @JvmStatic
    @BindingAdapter(value = ["webViewListener","webViewUri"], requireAll = false)
    fun webViewShow(view:DWebView?,webViewListener:DWebView.OnDWebViewListener?,webViewUri:String?){
        view?.setOnDWebViewListener(webViewListener)
        if(!webViewUri.isNullOrEmpty()){
            view?.loadUrl(webViewUri)
        }
    }

    /**
     * 使用x5内核加载文件
     */
    @JvmStatic
    @BindingAdapter(value = ["webViewShowFile"], requireAll = false)
    fun webViewShowFile(view:DWebView?,webViewShowFile:Boolean=false){

    }

    /**
     *  JsBridge，js调用原生接口
     */
    @JvmStatic
    @BindingAdapter(value = ["webViewJsBridge"], requireAll = false)
    fun webViewJsBridge(view:DWebView?,bridge:JsCallNativeApi?){
        view?.addJavascriptObject(bridge,"jsApi")
    }

    /**
     *  JsBridge，原生调用js，无传参有回调
     */
    @JvmStatic
    @BindingAdapter(value = ["webViewCallJsMethod","webViewNativeCallJsApi"], requireAll = false)
    fun webViewCallJs(view: DWebView?, webViewCallJsMethod: String, webViewNativeCallJsApi: NativeCallJsApi?) {
        view?.callHandler(webViewCallJsMethod, OnReturnValue<Boolean?> {
            webViewNativeCallJsApi?.callJsBack(webViewCallJsMethod, it)
        })
    }



}