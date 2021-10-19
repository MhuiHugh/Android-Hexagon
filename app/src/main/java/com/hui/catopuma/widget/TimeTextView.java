package com.hui.catopuma.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import com.blankj.utilcode.util.TimeUtils;
import com.hui.core.utils.CoreConfig;
import com.hui.core.utils.LogUtils;

import kotlin.jvm.Volatile;

/**
 * 时间每秒自动更新
 * Created by Mhui on 2016/7/18.
 */
public class TimeTextView extends AppCompatTextView {

    /**
     * TextView显示内容
     */
    @Volatile
    private String textStr = "";

    /**
     * 最后一次显示内容，优化刷新 相同内容不更新
     */
    @Volatile
    private String lastTextStr = "";

    /**
     * 传入的倒计时时间
     */
    private long inputTime = 0L;

    /**
     * 倒计时总毫秒数
     */
    @Volatile
    private long endMillisecond = 0L;

    /**
     * 倒计时总毫秒数
     */
    private long endMillisecondNoChange = 0L;

    /**
     * 时间是否是时间戳，否为倒计时总时长
     */
    private boolean timestamp = false;

    /**
     * 倒计时精度，轮询间隔 根据显示刷新 时分秒，毫秒调整精度
     */
    private int precision = 1000;

    /**
     * 判断结束精度，毫秒
     */
    private int endPrecision = 1000;

    /**
     * 倒计时时间精度
     */
    public static enum TimePrecision {MINUTE_30, MINUTE_1, SECOND_1, MILLISECOND_500, MILLISECOND_200}

    /**
     * 字符串id
     */
    private int strId = 0;

    /**
     * 时间戳格式化
     */
    private String timeFormat = CoreConfig.TIME_FORMAT_S;

    TimeThread timeThread;
    OnTimeZeroListener listener;

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (timeThread == null) {
            timeThread = new TimeThread();
        }
        timeThread.start();
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Handler realNameHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            //LogUtil.v(""+msg.what);
            switch (msg.what) {
                case 0: //更新发送验证码时间
                    lastTextStr = textStr;
                    setText(textStr);
                    break;
                case 1://监听回调
                    if (listener != null) {
                        listener.onTimeZero(TimeTextView.this);
                        realNameHandler.sendEmptyMessageDelayed(2, precision);
                    }
                    break;
                case 2:
                    break;
            }
        }
    };

    /**
     * 设置TextView 内容
     *
     * @param mtime      时间，毫秒
     * @param mtimestamp 是否是时间戳
     * @param mstrId     字符串id
     * @param mPrecision 时间刷新精度
     * @param format     时间格式
     */
    public TimeTextView setTextStr(long mtime, boolean mtimestamp, int mstrId, TimePrecision mPrecision, String format) {
        inputTime = mtime;
        timestamp = mtimestamp;
        strId = mstrId;
        timeFormat = format;

        if (timestamp) {
            //时间戳,Math.abs 距离开始|距结束
            endMillisecond = Math.abs(TimeUtils.getNowMills() - mtime);
            endMillisecondNoChange = endMillisecond;
        } else {
            endMillisecond = mtime;
            endMillisecondNoChange = endMillisecond;
        }

        switch (mPrecision) {
            case MINUTE_30:
                //30分钟
                precision = 30 * 60 * 1000;
                break;
            case MINUTE_1:
                //1分钟
                precision = 60 * 1000;
                break;
            case SECOND_1:
                //1秒钟
                precision = 1000;
                break;
            case MILLISECOND_500:
                //500 毫秒
                precision = 500;
                endPrecision = 500;
                break;
            case MILLISECOND_200:
                //200毫秒
                precision = 200;
                endPrecision = 200;
                break;
        }
        return this;
    }

    public class TimeThread extends Thread {
        public boolean exit = false;

        @Override
        public void run() {
            long endTime = 0L;
            //开始时重置倒计时时间
            endMillisecond = endMillisecondNoChange;
            do {
                try {
                    endTime = endMillisecond - precision;
                    if (endTime > -precision) {
                        textStr = getResources().getString(strId, TimeUtils.millis2String(endTime, TimeUtils.getSafeDateFormat(timeFormat)));
                        realNameHandler.removeMessages(0);
                        if (!textStr.equals(lastTextStr)) {
                            realNameHandler.sendEmptyMessage(0);
                        }
                        //状态切换
                        if (endTime < endPrecision && listener != null) {
                            realNameHandler.sendEmptyMessageDelayed(1, precision);
                            Thread.sleep(precision);
                        }
                    }
                    //总时间递减
                    endMillisecond-=precision;
                    Thread.sleep(precision);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!exit);
        }
    }

    /**
     * 停止时间更新
     */
    public void stopFreshenTime() {
        if (timeThread != null) {
            try {
                timeThread.exit = true;
                timeThread.join();
                timeThread = null;
            } catch (InterruptedException ex) {
                LogUtils.e("stopFreshenTime:" + ex);
            }
        }
    }

    /**
     * 重新开始时间更新
     */
    public void startFreshenTime() {
        try {
            if (timeThread != null) {
                timeThread = null;
            }
            if(0==strId){
                LogUtils.e("TimeTextView strId 0!");
                return;
            }
            timeThread = new TimeThread();
            timeThread.start();
        } catch (Exception ex) {
            LogUtils.e("startFreshenTime:" + ex);
        }
    }

    public interface OnTimeZeroListener {
        void onTimeZero(TimeTextView view);
    }

    public TimeTextView setOnTimeZeroListener(OnTimeZeroListener timeZeroListener) {
        listener = timeZeroListener;
        return this;
    }

}
