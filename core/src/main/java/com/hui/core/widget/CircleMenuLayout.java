package com.hui.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hui.core.R;

/**
 * @author zhy
 *   http://blog.csdn.net/lmj623565791/article/details/43131133
 */
public class CircleMenuLayout extends ViewGroup {

    private final String TAG = this.getClass().getSimpleName();

    private int mRadius;//视图直径
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;

    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding;
    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 270;

    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mTmpAngle;
    /**
     * 检测按下到抬起时使用的时间
     */
    private long mDownTime;

    /**
     * 判断是否正在自动滚动
     */
    private boolean isFling;

    /**
     * 刚开始拖动处理
     */
    private boolean isFirstMove = true;
    /**
     * 每个item的弧度值
     */
    private float angleDelay = 0;

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG, "CircleMenuLayout()");
        // 无视padding
        setPadding(0, 0, 0, 0);
    }

    /**
     * 设置布局的宽高，并策略menu item宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(TAG, "omMeasure()");
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            // 主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            // 如果都设置为精确值，则直接取小值；
            resWidth = resHeight = Math.min(width, height);
        }
        setMeasuredDimension(resWidth, resHeight);
        // 获得直径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());
        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                    childMode);
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }
        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }


    /**
     * 设置menu item的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.v(TAG, "onLayout()");
        int layoutRadius = mRadius;

        // Laying out the child views
        final int childCount = getChildCount();

        int left, top;
        // menu item 的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        // 根据menu item的个数，计算角度
        angleDelay = 360 / (getChildCount() - 1);

        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getId() == R.id.id_circle_menu_item_center)
                continue;
            if (child.getVisibility() == GONE) {
                continue;
            }
            mStartAngle %= 360;
            // 计算，中心点到menu item中心的距离
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;
            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            // 叠加尺寸
            mStartAngle += angleDelay;
        }
    }

    /**
     * 记录上一次的x，y坐标
     */
    private float mLastX;
    private float mLastY;

    /**
     * 自动滚动的Runnable
     */
    private AutoFlingRunnable mFlingRunnable;

    /**
     * Touch 事件分发
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.v(TAG, "dispatchTouchEvent()");
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG,"dispatchTouch down");
                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

                // 如果当前已经在快速滚动
                if (isFling) {
                    // 移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG,"dispatchTouch move");
                //判断是否在圆环内滑动
                float a = Math.abs(x - mRadius / 2);
                float b = Math.abs(y - mRadius / 2);
                //勾股定律求斜边长度
                float c = (float) Math.sqrt(a * a + b * b);
                //等边直角三角形斜边长度
                float d = (float) Math.sqrt(2 * (mRadius * RADIO_DEFAULT_CHILD_DIMENSION));
                //圆环内滑动判断
                if (c > mRadius / 2 || c < d) {
                 //Log.v(TAG, "未在圆环内！");
                    return true;
                }

                //区别点击和滑动，减少误操作
                float g = Math.abs(x - mLastX);
                float h = Math.abs(y - mLastY);
                if (isFirstMove) {
                    if (Math.sqrt(g * g + h * h) > Math.abs(mRadius / 16)) {
                        isFirstMove = false;
                        return true;
                    }else{
                        return  false;
                    }
                }
                /**
                 * 获得开始的角度
                 */
                float start = getAngle(mLastX, mLastY);
                /**
                 * 获得当前的角度
                 */
                float end = getAngle(x, y);
                // 如果是一、四象限，则直接end-start，角度值都是正值
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                } else
                // 二、三象限，色角度值是负值
                {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }

                // 重新布局
                requestLayout();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG,"dispatchTouch up");
                isFirstMove = true;
                // 计算，每秒移动的角度
                float anglePerSecond = mTmpAngle * 1000
                        / (System.currentTimeMillis() - mDownTime);

                // 如果达到该值认为是快速移动
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                    // post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                    return false;
                }else{
                    correctPosition();
                    requestLayout();
                }

                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * onTouch 事件拦截
     *
     * @param event
     * @return
     */
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG,"onIntercept down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG,"onIntercept move");
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG,"onIntercept up");
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

	/**
	 * Touch事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG,"touch down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v(TAG,"touch move");
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG,"touch up");
                break;
        }
		return super.onTouchEvent(event);
	}

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }


    /**
     * 如果每秒旋转角度到达该值，则认为是自动滚动
     *
     * @param mFlingableValue
     */
    public void setFlingableValue(int mFlingableValue) {
        this.mFlingableValue = mFlingableValue;
    }

    /**
     * 设置内边距的比例
     *
     * @param mPadding
     */
    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    /**
     * 自动滚动的任务
     *
     * @author zhy
     */
    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        public void run() {
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20) {
                if (mStartAngle%30 == 0) {
                    isFling = false;
                    return;
                } else {
                    angelPerSecond = 0;
                    correctPosition();
                }
            }
            isFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }
    }

    /**
     * @author MhuiHugh
     * 视图位置校准
     */
    private void correctPosition() {
        //修复mStartAngle>360时位置矫正异常bug
        mStartAngle %= 360;
        //------------算法一,顶部2个六边形平行
//        int temp=-1;
//        float last=-1;
//        //位置需要校准
//         for(int i=0;i<getChildCount();i++){
//           if(last>Math.abs(mStartAngle-angleDelay*i)){
//               temp=i;
//           }
//             last=(float)Math.abs(mStartAngle-angleDelay*i);
//         }
//        mStartAngle=temp*angleDelay;
        //----------------算法二,顶部1个六边形
        int last=0;
        float min=360;
        int point[]={30,90,150,210,270,330};
        for(int i=0;i<6;i++){
            float temp=(float)Math.abs(mStartAngle-point[i]);
            if(temp<min){
                min=temp;
                last=i;
            }
        }
        mStartAngle=point[last];
    }

}
