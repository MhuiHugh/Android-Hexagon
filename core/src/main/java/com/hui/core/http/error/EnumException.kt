package com.hui.core.http.error

/**
 * @Author petterp
 * @Date 2020/6/23-1:32 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 常见的报错情况
 */
enum class EnumException {

    //网络断开
    NET_DISCONNECT,

    //网络不可用
    NET_UNAVAILABLE,

    //连接超时
    CONNECT_EXCEPTION,

    //网络异常
    SOCKET_EXCEPTION,

    //请求超时
    TIMEOUT_EXCEPTION,

    //数据解析错误
    JSON_EXCEPTION,

    //未知异常
    NO_KNOW_EXCEPTION
}