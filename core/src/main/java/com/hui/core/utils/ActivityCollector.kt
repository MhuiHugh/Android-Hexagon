package com.hui.core.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import java.lang.ref.WeakReference
import java.util.*

/**
 * 应用中所有Activity的管理器，可用于一键杀死所有Activity。
 *
 */
object ActivityCollector {

    private val activityList = ArrayList<WeakReference<Activity>?>()

    fun size(): Int {
        return activityList.size
    }

    fun add(weakRefActivity: WeakReference<Activity>?) {
        activityList.add(weakRefActivity)
    }

    fun remove(weakRefActivity: WeakReference<Activity>?) {
        activityList.remove(weakRefActivity)
    }

    fun finishAll() {
        if (activityList.isNotEmpty()) {
            for (activityWeakReference in activityList) {
                val activity = activityWeakReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }
            activityList.clear()
        }
    }

    /**
     * 设置当前屏幕方向为横屏
     */
     fun setHorizontalScreen( activity:Activity) {
        if (activity.requestedOrientation!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    /**
     * 设置当前屏幕方向为竖屏
     */
    fun setVerticalScreen(activity:Activity) {
        if (activity.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


}
