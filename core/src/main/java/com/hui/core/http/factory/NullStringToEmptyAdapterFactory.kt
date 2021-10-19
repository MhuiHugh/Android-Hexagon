package com.hui.core.http.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken


/**
 * @Author petterp
 * @Date 2020/7/8-12:09 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 类型转换工厂
 */
class NullStringToEmptyAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
        return when (type.rawType) {
            String::class.java -> StringAdapter() as TypeAdapter<T>
            Int::class.java -> IntegerAdapter() as TypeAdapter<T>
            Boolean::class.java -> BooleanAdapter() as TypeAdapter<T>
            Float::class.java -> FloatAdapter() as TypeAdapter<T>
            Long::class.java -> LongAdapter() as TypeAdapter<T>
            Double::class.java -> DoubleAdapter() as TypeAdapter<T>
            else -> null
        }
    }
}
