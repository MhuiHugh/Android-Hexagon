package com.hui.core.http.error

import android.util.SparseArray
import androidx.core.util.putAll
import com.hui.core.http.net.NetObserver
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 服务器错误码处理
 */
object ErrorHttpKtx {

    private val mSparseArray = SparseArray<CodeBean>()
    private val mErrorMap = HashMap<EnumException, suspend () -> Unit>()

    /** 保存全局数据响应码 */
    internal fun putCode(code: Int, codeBean: CodeBean): ErrorHttpKtx {
        mSparseArray.put(code, codeBean)
        return this
    }

    internal fun putCodeAll(codeArray: SparseArray<CodeBean>) {
        mSparseArray.putAll(codeArray)
    }

    fun getCode(code: Int): CodeBean? {
        return mSparseArray[code]
    }

    /** 保存全局异常码,有些异常可能会统一处理[enumException] */
    internal fun putError(enumException: EnumException, obj: suspend () -> Unit) {
        mErrorMap[enumException] = obj
    }

    suspend fun invokeError(t: Throwable) {
        //先判断网络是否关闭
        if (!NetObserver.isNetEnable()) {
            mErrorMap[EnumException.NET_DISCONNECT]?.invoke()
            return
        }

        val error = when (t) {
            is ConnectException -> EnumException.CONNECT_EXCEPTION
            is SocketTimeoutException -> EnumException.SOCKET_EXCEPTION
            is JSONException -> EnumException.JSON_EXCEPTION
            is TimeoutException -> EnumException.TIMEOUT_EXCEPTION
            else -> EnumException.NO_KNOW_EXCEPTION
        }

        //通常这种报错一般先ping一下网络再执行最终报错
        if (error == EnumException.NO_KNOW_EXCEPTION) {
            //再ping一下网络
            if (!NetObserver.isAvailable()) {
                mErrorMap[EnumException.NET_UNAVAILABLE]?.invoke()
                return
            }
        }
        //最终执行网络方式
        mErrorMap[error]?.invoke()
    }
}
