package  com.hui.catopuma.data.bean

import com.hjq.toast.ToastUtils
import com.hui.catopuma.data.Constant
import com.hui.core.http.error.ErrorHttpKtx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author: Mhui
 * @Date: 2020/4/30 15:22
 * @Desc:
 */

//data class Response<out T>(val bean:ResponseBean,val data:T)

data class ResponseBean<T>(
    val code: Int,
    val msg: String?,
    val result: T?
){
    val success: Boolean get()= (Constant.HTTP_SUCCESS == code)
}

/**
 * 同步，必须在协程内
 */
suspend inline fun <T> ResponseBean<T>.block(onSuccess: (T?)->Unit?,noinline onError: ((T?) -> Unit?)? = null,isToast: Boolean = true) {
    if (success) {
        onSuccess(result)
    } else {
        if (isToast&&!msg.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                ToastUtils.show(msg)
            }
        }
        //这里优先执行方法内的错误处理
        onError?.let {
            onError
        }
        //一般情况下，执行的都是全局异常处理，这里也是挂起函数，便于可能存在的耗时操作
        ErrorHttpKtx.getCode(code)?.let {
            if(!msg.isNullOrEmpty()) {
                it.obj(msg)
            }
        }
    }
}

/**
 * 异步，结果切回main线程,使用场景并不多
 */
suspend fun <T> ResponseBean<T>.asyncBlockMain(onSuccess: (T?) -> Unit,onError: (T?) -> Unit,isToast: Boolean = true) {
    withContext(Dispatchers.Main) {
        syncBlock(onSuccess, onError, isToast)
    }
}

/**
 * 异步,结果切回IO线程
 * */
suspend fun <T> ResponseBean<T>.syncBlockIO(onSucces: (T?) -> Unit,onError: (T?) -> Unit,isToast: Boolean = true) {
    withContext(Dispatchers.IO) {
        syncBlock(onSucces, onError, isToast)
    }
}

/**
 * 异步，结果处于默认线程,一般这种情况下，线程都是处于io
 * [success]的回调里可以使用LiveData将数据发射出去
 *
 */
suspend inline fun <T> ResponseBean<T>.syncBlock(onSuccess: (T?) -> Unit, noinline onError: ((T?) -> Unit?)? = null,isToast: Boolean = true) {
    if (success) {
         onSuccess(result)
    } else {
        if (isToast&&!msg.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                ToastUtils.show(msg)
            }
        }
        onError?.let {
            it(result)
        }
        ErrorHttpKtx.getCode(code)?.let {
            if(!msg.isNullOrEmpty()) {
                it.obj(msg)
            }
        }
    }
}