package com.hui.core.http.factory

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okio.IOException


/**
 * @Author petterp
 * @Date 2020/7/8-12:09 PM
 * @Email ShiyihuiCloud@163.com
 * @Function null值处理器
 */
internal class StringAdapter : TypeAdapter<String?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: String?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): String {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return ""
        }
        return `in`.nextString()
    }
}

internal class IntegerAdapter : TypeAdapter<Int?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Int?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Int {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return 0
        }
        return `in`.nextInt()
    }
}

internal class LongAdapter : TypeAdapter<Long?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Long?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Long {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return 0L
        }
        return `in`.nextLong()
    }
}

internal class FloatAdapter : TypeAdapter<Float?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Float?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Float {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return 0f
        }
        return `in`.nextString().toFloat()
    }
}



internal class BooleanAdapter : TypeAdapter<Boolean?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Boolean?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Boolean {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return false
        }
        return `in`.nextBoolean()
    }
}


internal class DoubleAdapter : TypeAdapter<Double?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Double?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Double {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return 0.0
        }
        return `in`.nextDouble()
    }
}