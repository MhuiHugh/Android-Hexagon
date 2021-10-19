package com.hui.core.http.file

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Created by Petterp
 * on 2020-01-19
 * Function:
 */
/**
 * 多文件上传，文件类型应该框架自动推导，暂时todo,需手动传入
 */
data class FileBean(
    var file: File,
    var fileName: String = file.name,
    var fileType: String = "image/jpg"
)

inline fun fileBody(obj: () -> FileBean): MultipartBody.Part {
    with(obj()) {
        val requestBody: RequestBody = file.asRequestBody(fileType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }
}

inline fun fileBodys(obj: () -> List<FileBean>): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>(3)
    obj().forEach {
        list += fileBody { it }
    }
    return list
}

