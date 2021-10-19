package com.hui.catopuma.page.web

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.*
import com.hui.catopuma.R
import com.hui.catopuma.viewmodel.BaseViewModel
import com.hui.core.http.error.launchVMHttp
import com.hui.core.http.file.DownloadFileKtx
import com.hui.core.http.file.over
import com.hui.core.http.file.overSchedule
import com.hui.core.utils.LogUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

/**
 * WebViewModel
 */
class WebViewModel : BaseViewModel() {

    /**
     * 是否显示标题栏
     */
    val showTitleBar: ObservableBoolean=ObservableBoolean(true)

    /**
     * 是否显示标题栏底部分割线
     */
    val showTitleBarLine: ObservableBoolean=ObservableBoolean(true)

    /**
     * 右侧按钮id
     */
    val leftActionDrawableId:ObservableInt=ObservableInt(R.drawable.bar_arrows_left_black)

    /**
     * 标题
     */
    val title:ObservableField<String> =ObservableField<String>("")

    /**
     * 右侧文本按钮
     */
    val rightActionString: ObservableField<String> = ObservableField<String>("")

    /**
     * 右侧按钮id
     */
    val rightActionDrawableId:ObservableInt=ObservableInt()

    /**
     * 是否显示加载状态
     */
    val showLoadProgress:ObservableBoolean=ObservableBoolean(true)

    /**
     * 网页加载进度
     */
    val loadProgress:ObservableInt=ObservableInt(0)

    /**
     * 加载地址
     */
    val webUri:ObservableField<String> =ObservableField<String>()

    /**
     * 原生调js方法
     */
    val callJsMethod:ObservableField<String> =ObservableField<String>("sayhi")

    val downloadFilePath: UnPeekLiveData<String?> = UnPeekLiveData<String?>()

    init {

    }

    /**
     * 文件下载
     */
    fun downLoadFile(uri:String) {
        val fileType= FileUtils.getFileExtension(uri)
        LogUtils.v("文件类型：$fileType")
        launchVMHttp {
            requestHttp.downLoadFile(uri)
                .over(DownloadFileKtx("${TimeUtils.getNowString()}.${fileType}", AppUtils.getAppPackageName())).let{
                    withContext(Dispatchers.Main) {
                        LogUtils.v("文件地址: $it")
                        val path=UriUtils.uri2File(it).absolutePath
                        LogUtils.v("文件路径: $path")
                        downloadFilePath.value=path
                    }
                }
        }
    }

}


