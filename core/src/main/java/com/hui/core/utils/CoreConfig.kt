package com.hui.core.utils


/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/116:50
 *    desc   :框架配置
 */
object CoreConfig {

    const val TAG = "h3xm"

    /**
     * DataStore 创建key value文件名称
     */
    const val DATA_STORE_NAME = "data_catopuma"

    /**
     * 拆单组靠右贴边？
     */
    const val STORE_CIRCLE_RIGHT="store_circle"

    /**
     * okhttp 设置
     */
    const val HTTP_CONNECT_TIME=30L
    const val HTTP_READ_TIME=30L
    const val HTTP_WRITE_TIME=30L

    /**
     * 状态管理
     */
    object StatusLayout {
        const val LOADING = 100
        const val EMPTY = 101
        const val ERROR = 102
        const val SUCCESS = 103

        const val CUSTOMER = 110
    }

    /**
     * 时间格式化
     */
    const val TIME_FROMAT_DAY = "yyyy-MM-dd" //年月日
    const val TIME_FORMAT_WITH_NORMAL = "yyyy-MM-dd HH:mm:ss" //年月日 时分秒
    const val TIME_FORMAT_WITH_TIME_ZONE = "yyyy-MM-dd HH:mm:ss Z" //年月日 时分秒 时区
    const val TIME_FORMAT_HI = "yyyy-MM-dd-HH-mm-ss"
    const val TIME_FORMAT_WITH_HM = "HH:mm" //时分
    const val TIME_FORMAT_MDT = "MM-dd HH:mm" //月日时分
    const val TIME_FORMAT_SS = "ss" //秒
    const val TIME_FORMAT_S = "s" //秒


}