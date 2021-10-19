package com.hui.core.http.error

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 异常处理方法
 * 暂时在这里
 */
data class CodeBean( val obj:suspend (String) -> Unit = { })