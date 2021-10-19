package com.hui.core.http.error

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Retrofit异常处理
 * //参考自掘金
 */
private suspend fun tryCatch(
    finalBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend (e: Throwable) -> Unit)? = null
    , tryBlock: suspend CoroutineScope.() -> Unit

) {
    try {
        coroutineScope {
            tryBlock()
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        errorBlock?.let {
            it(t)
            return
        }
        //执行全局报错
        ErrorHttpKtx.invokeError(t)
    } finally {
        finalBlock?.invoke()
    }
}

/** 适用于ViewModel的http作用域,注意，这里只是为了便于[单纯]发起一个网络请求
 *  如果本身已经处于协程体，请使用[launchHttp]
 * */
fun ViewModel.launchVMHttp(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    finalBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend (e: Throwable) -> Unit)? = null
    , tryBlock: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(coroutineContext) {
        tryCatch(finalBlock, errorBlock, tryBlock)
    }
}

///** 适用于协程范围的http作用域,作用域取决与内部*/
//suspend fun CoroutineScope.launchHttp(
//    finalBlock: suspend () -> Unit = {},
//    errorBlock: suspend (e: Throwable) -> Unit = {},
//    tryBlock: suspend CoroutineScope.() -> Unit
//) {
//    tryCatch(errorBlock, finalBlock, tryBlock)
//}


/** 适用于Lifecycle-ktx 的http作用域,注意，这里只是为了便于[单纯]发起一个网络请求
 *  如果本身已经处于协程体，请使用[launchHttp]
 * */
fun LifecycleOwner.launchLfHttp(
    context: CoroutineContext = Dispatchers.IO,
    finalBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend (e: Throwable) -> Unit)? = null
    , tryBlock: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) {
        tryCatch(finalBlock, errorBlock, tryBlock)
    }
}

/** 适用于非常规情况下，这种场景其实并不安全，无法保证在ui销毁之后，网络请求自动关闭
 *  适用于在 ViewMode-ktx.Lifecycle-Ktx 扩展情况下使用
 *  还有一种常见场景，有些时候，我们可能处于dataSource中，此时推荐使用者自行创建一个全局的协程作用域。
 *  示例
 *  val ktxScope=CoroutineScope(Dispatchers.IO)
 *  ktxScope.launch{
 *      launchHttp{
 *
 *      }
 *  }
 *
 *  通常情况下，net不应该提供网络发起前的方法，net只是处理网络，它依附于上层的作用域，故这里并没有自己去开作用域
 *  错误示例,这种做法看似没有问题，但这种使用方式，不予推荐，这种行为不应让网络层处理
 *  fun ViewModel.launchVMHttp(
 *       coroutineContext: CoroutineContext = Dispatchers.IO,
 *       startBlock: suspend() ->Unit={},
 *       endBlock:suspend()->Unit={},
 *       finalBlock: suspend () -> Unit = {},
 *       errorBlock: suspend (e: Throwable) -> Unit = {},
 *       tryBlock: suspend CoroutineScope.() -> Unit
 *      ) {
 *       viewModelScope.launch(coroutineContext) {
 *          //网络请求前
 *          startBlock()
 *          tryCatch(errorBlock, finalBlock, tryBlock)
 *          //网络结束
 *          withContextMain{
 *           endBlock()
 *        }
 *      }
 *   }
 */
suspend fun launchHttp(
    finalBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend (e: Throwable) -> Unit)? = null
    , tryBlock: suspend CoroutineScope.() -> Unit
) {
    tryCatch(finalBlock, errorBlock, tryBlock)
}
