package com.hui.core.http.net

/**
 * @Author petterp
 * @Date 2020/6/23-2:19 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 监听网络是否关闭
 */
interface INetEnable {
    fun netOpen()
    fun netOff()
}