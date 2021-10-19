package com.hui.catopuma.data.bean

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/5/28 10:49
 *    desc   :
 */
data class Ad(
    val action: String,
    val imageUrl: String,
    val title: String,
    var adType:Int=100,
    val imageDrawableId:Int
)

data class Ads(
    val ad: Ad
)

