package com.hui.core.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


/**
 *框架日志输出控制
 */
object CoreLog {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    const val NOTHING = 6
    private const val KEY_TAG = "h3xm"

    //控制log等级
    var LEVEL = VERBOSE
    fun init(level: Int = VERBOSE) {
        LEVEL = level
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    @JvmStatic
    fun v(tag: String = KEY_TAG, message: String) {
        if (LEVEL <= VERBOSE) {
            Logger.t(tag).v(message)
        }
    }

    @JvmStatic
    fun d(tag: String = KEY_TAG, message: Any) {
        if (LEVEL <= DEBUG) {
            Logger.t(tag).d(message)
        }
    }

    @JvmStatic
    fun i(tag: String = KEY_TAG, message: String) {
        if (LEVEL <= INFO) {
            Logger.t(tag).i(message)
        }
    }

    @JvmStatic
    fun w(tag: String = KEY_TAG, message: String) {
        if (LEVEL <= WARN) {
            Logger.t(tag).w(message)
        }
    }

    @JvmStatic
    fun json(tag: String = KEY_TAG, message: String) {
        if (LEVEL <= WARN) {
            Logger.t(tag).json(message)
        }
    }

    @JvmStatic
    fun e(tag: String = KEY_TAG, message: String) {
        if (LEVEL <= ERROR) {
            Logger.t(tag).e(message)
        }
    }
}
