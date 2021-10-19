package com.hui.catopuma.data

import com.hui.core.utils.DataStoreUtils

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/316:57
 *    desc   :全局变量
 */
object Config {

    ///登录用户token
    private var USER_TOKEN = ""

    fun getUserToken(): String {
        if (USER_TOKEN.isNullOrEmpty()) {
            USER_TOKEN = DataStoreUtils.readStringData(Constant.KEY_USER_TOKEN, "")
        }
        return USER_TOKEN
    }

    fun setUserToken(token: String?) {
        USER_TOKEN = if (token.isNullOrEmpty()) "" else token
        DataStoreUtils.putSyncData(Constant.KEY_USER_TOKEN, token)
    }


}