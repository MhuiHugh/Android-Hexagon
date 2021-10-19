package com.hui.core.utils

import java.text.DecimalFormat

/**
 * 扩展函数,扩展方法
 */


/**
 * ----------------String 字符串扩展
 */
fun String.moneyValue(): String {
    return try {
        val result = if (this.isNullOrBlank() || this == "null") {
            "0.00"
        } else {
            this.replace(",", "")
        }.toDouble()
        val df = DecimalFormat("0.00")
        df.format(result)
    } catch (e: Exception) {
        "0.00"
    }
}
