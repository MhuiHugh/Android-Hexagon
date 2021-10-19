package com.hui.catopuma.databind

import androidx.databinding.BindingAdapter
import com.hui.catopuma.widget.TimeTextView
import com.hui.core.utils.CoreConfig

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1214:59
 *    desc   :TimeTextView 控件
 */
object TimeTextViewDatabindAdapter {

    /**
     * TimeTextView 倒计时组件
     * @param mtime      时间，毫秒
     * @param mtimestamp 是否是时间戳
     * @param mstrId     字符串id
     * @param mPrecision 时间刷新精度
     * @param format     时间格式
     */
    @JvmStatic
    @BindingAdapter(value = ["time", "timestamp", "timeStrId", "timePrecision", "timeFormat", "timeZeroListener"],requireAll = false)
    fun timeText(
        view: TimeTextView,
        time: Long?,
        timestamp: Boolean? = false,
        timeStrId: Int?,
        timePrecision: TimeTextView.TimePrecision? = TimeTextView.TimePrecision.MILLISECOND_200,
        timeFormat: String? = CoreConfig.TIME_FORMAT_S,
        timeZeroListener: TimeTextView.OnTimeZeroListener? ) {
        //LogUtils.v("timeText $view $time $timestamp $timeStrId $timeFormat")
        view.setTextStr(time ?: 5000, timestamp ?: false, timeStrId ?: 0, timePrecision, timeFormat)
        view.setOnTimeZeroListener(timeZeroListener)
    }

    /**
     * 控制倒计时
     */
    @JvmStatic
    @BindingAdapter(value = ["timeStart"], requireAll = false)
    fun timeTextViewStart(view: TimeTextView, timeStart: Boolean = true) {
        //LogUtils.v("timeTextViewStart  $timeStart")
        if (timeStart) {
            view.startFreshenTime()
        } else {
            view.stopFreshenTime()
        }
    }
}