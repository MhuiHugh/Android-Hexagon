@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.hui.core.http.file

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.hui.core.http.LiveConfig
import com.hui.core.http.interceptor.LiveLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.*
import java.lang.Exception

/** 具体的文件数据类 */
data class DownloadFileKtx(
    /**
     * 文件名,需要后缀
     */
    var fileName: String,

    /**
     * 文件保存路径
     */
    var path: String = LiveConfig.config.downloadName

)


/**
 * Function: 文件操纵类
 * Android P 测试通过
 * Android 10 测试通过
 */
class DownloadListener(private var mTotalLength: Long) {
    var mDownloadLength: Long = 0
    private var scaleInt = 0

    fun getFileInt(): Int {
        val size = ((mDownloadLength.toDouble() / mTotalLength) * 100).toInt()
        scaleInt = size
        return scaleInt
    }

}

object DownloadResponseKtx {

    /**
     * 保存至Q
     */
    @SuppressLint("NewApi")
    suspend inline fun saveQ(
        body: ResponseBody,
        downloadFileDow: DownloadFileKtx,
        fileInfo: DownloadListener? = null,
        flow: FlowCollector<Int>? = null
    ): Uri {
        with(downloadFileDow) {
            val values = ContentValues()
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/$path/")
            val external = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val resolver = LiveConfig.config.mContext.contentResolver
            val insertUri = resolver.insert(external, values)
            resolver.openOutputStream(insertUri!!)?.let {
                file(it, body.byteStream(), fileInfo, flow)
            }
            return insertUri
        }
    }


    /**
     * Q以下的保存文件
     */
    suspend inline fun saveFile(
        body: ResponseBody,
        downloadFileDow: DownloadFileKtx,
        fileInfo: DownloadListener? = null,
        flow: FlowCollector<Int>? = null): Uri {
        with(downloadFileDow) {
            val f =
                File(Environment.getExternalStorageDirectory().path + "/" + path + "/")
            if (!f.exists()) {
                f.mkdir()
            }
            val file = File(f.parent + "/" + path + "/" + fileName)
            file(FileOutputStream(file), body.byteStream(), fileInfo, flow)
            return Uri.fromFile(file)
        }
    }


    /**
     * 共用文件写入
     */
    suspend inline fun file(
        os: OutputStream,
        ios: InputStream,
        fileInfo: DownloadListener?,
        flow: FlowCollector<Int>?) {
        //下载进度
        var downloadSiz = 0L
        try {
            var read: Int
            var sizeCopy = 0
            val buffer = ByteArray(8192)
            while (ios.read(buffer).apply { read = this } > 0) {
                os.write(buffer, 0, read)
                fileInfo?.let {
                    downloadSiz += read
                    fileInfo.mDownloadLength = downloadSiz
                    val siz = fileInfo.getFileInt()
                    if (siz > sizeCopy) {
                        flow?.emit(siz)
                        sizeCopy = siz
                    }
                }
            }
        } catch (e: Exception) {
            LiveLog.d("liveHttp", e.message.toString())
        } finally {
            os.close()
            ios.close()
        }
    }
}

/**
 * 带进度的文件下载
 */
@ExperimentalCoroutinesApi
suspend fun ResponseBody.overSchedule(
    downloadFileDow: DownloadFileKtx,
    obj: ((Uri) -> Unit) = {}): Flow<Int> {
    val body = this
    return flow<Int> {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            val saveQ = DownloadResponseKtx.saveQ(
                body,
                downloadFileDow,
                DownloadListener(body.contentLength()),
                this
            )
            withContext(Dispatchers.Main) {
                obj(saveQ)
            }
        } else {
            val saveFile = DownloadResponseKtx.saveFile(
                body,
                downloadFileDow,
                DownloadListener(body.contentLength()),
                this
            )
            withContext(Dispatchers.Main) {
                obj(saveFile)
            }
        }
    }.flowOn(Dispatchers.IO)
}

/**
 * 直接返回uri的文件下载
 */
suspend inline fun ResponseBody.over(downloadFileDow: DownloadFileKtx): Uri {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        DownloadResponseKtx.saveQ(this, downloadFileDow)
    } else {
        DownloadResponseKtx.saveFile(this, downloadFileDow)
    }
}