package com.hui.core.widget.circle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;

/**
 * 菜单根布局，处理菜单全屏滑动事件监听，菜单组显示隐藏
 * Created by Menghui on 2016/8/23.
 */
public class CircleRelativeLayout extends RelativeLayout {

    private final String TAG = this.getClass().getSimpleName();

    ///Circle控件，子View移动状态父布局不再拦截onTouch事件
    public static volatile boolean isCircleChildMoved = false;
    /**
     * 左右滑动的最短距离
     */
    private int distance = 100;
    /**
     * 上下滑动最大高度
     */
    private int maxMoveHeight = 30;
    /**
     * 左右滑动的最小速度
     */
    private int velocity = 1200;//500毫秒内移动X轴超过800像素（打日志自己评估，最快划过720手机速度大概2000）
    private VelocityTracker mVelocityTracker = null;//计算瞬时滑动速度

    private float downX = 0.0f;
    private float downY = 0.0f;
    private boolean intercept = false;//是否拦截事件分发

    private OnMoveListener listener;//事件监听
    private OnToRightListener toRightListener;

    //------------------------------------------------
    public CircleRelativeLayout(Context context) {
        super(context);
    }

    public CircleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != mVelocityTracker) {
            //释放
            mVelocityTracker.recycle();
        }
        super.onDetachedFromWindow();
    }
    //------------------------------------------------

    /**
     * 事件分发
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
        }
        mVelocityTracker.addMovement(ev);//将事件加入到VelocityTracker类实例中
        mVelocityTracker.computeCurrentVelocity(500);//设置时间间隔,500毫秒
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //瞬时X轴滑动速度
                float tempVelocity = Math.abs(mVelocityTracker.getXVelocity());
                //Log.v(TAG, "xVelocity:" + tempVelocity);
                if (ev == null) {
                    intercept = false;
                }
                //处理Y轴滑动距离过长情况
                if (Math.abs(downY - ev.getY()) > maxMoveHeight) {
                    intercept = false;
                }
                //子控件移动状态不再计算滑动事件
                if (!isCircleChildMoved) {
                    //Log.v(TAG, "move:" + (downX - ev.getX() > distance && tempVelocity > velocity) + " -- " + (downX - ev.getX()) + " -- " + tempVelocity);
                    // 向左滑
                    if (downX - ev.getX() > distance && tempVelocity > velocity) {
                        if (listener != null) {
                            //Log.v(TAG, "toLeft()");
                            listener.toLeft();
                        }
                        if(null!=toRightListener){
                            toRightListener.toRight(false);
                        }
                        intercept = true;
                    }
                    // 向右滑
                    if (ev.getX() - downX > distance && tempVelocity > velocity) {
                        if (listener != null) {
                            listener.toRight();
                        }
                        if(null!=toRightListener){
                            toRightListener.toRight(true);
                        }
                        intercept = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                downX = 0;
                intercept = false;
                //清理
                mVelocityTracker.clear();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 事件拦截
     *
     * @param ev
     * @return true拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.v(TAG, "onInterceptTouchEvent()" + Config.isCircleChildMoved);
        //子控件移动状态不拦截onTouch事件
        if (isCircleChildMoved) {
            return false;
        }
        return intercept;
    }

    /**
     * 事件响应
     *
     * @param l
     */
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    //--------监听接口---------------------------

    /**
     * 菜单组滑动显隐监听
     */
    public interface OnMoveListener {
        void toLeft();

        void toRight();
    }

    public void setOnMoveListenerListener(OnMoveListener listener) {
        this.listener = listener;
    }

    public void setOnToRightListener(OnToRightListener listener) {
        this.toRightListener = listener;
    }

    public interface OnToRightListener{
        void toRight(boolean toRight);
    }

}
