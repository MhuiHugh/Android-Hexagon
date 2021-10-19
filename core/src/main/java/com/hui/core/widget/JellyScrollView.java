package com.hui.core.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ScrollView加果冻效果，在事件分发中做逻辑处理，如果在事件拦截中需要子控件设置监听
 * 原创博客地址http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/1109/6759.html
 * Created by Menghui on 2017/2/20.
 */
public class JellyScrollView extends ScrollView {

    private View inner;// 子View

    private float y;// 点击时y坐标

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算

    private boolean isMoving = false;// 是否开始移动.

    private int top;// 拖动时时高度。

    private int mTouchSlop;//系统最少滑动距离

    public JellyScrollView(Context context) {
        super(context);
    }

    public JellyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JellyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    /**
     * 事件分发
     * @param ev
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent ev){
        if (inner != null) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    y = ev.getY();
                    top = 0;
                    break;
                case MotionEvent.ACTION_UP:
                    // 手指松开.
                    isMoving = false;
                    if (isNeedAnimation()) {
                        animation();
                    }
                    break;
                /***
                 * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
                 * 因为此时是ScrollView的touch事件传递到到了ListView的子item上面.所以从第二次计算开始.
                 * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
                 */
                case MotionEvent.ACTION_MOVE:
                    final float preY = y;// 按下时的y坐标
                    float nowY = ev.getY();// 每时刻y坐标
                    int deltaY = (int) (nowY - preY);// 滑动距离
                    if (!isCount) {
                        deltaY = 0; // 在这里要归0.
                    }
                    if (Math.abs(deltaY) < mTouchSlop && top <= 0) {
                        return true;
                    }
                    // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                    isNeedMove();
                    if (isMoving) {
                        // 初始化头部矩形
                        if (normal.isEmpty()) {
                            // 保存正常的布局位置
                            normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        }
                        // 移动布局
                        inner.layout(inner.getLeft(), inner.getTop() + deltaY / 3, inner.getRight(), inner.getBottom() + deltaY / 3);
                        top += (deltaY / 6);
                    }
                    isCount = true;
                    y = nowY;
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /***
     * 回缩动画
     */
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);

        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();

        // 手指松开要归0.
        isCount = false;
        y = 0;
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 是否需要移动布局
     * inner.getMeasuredHeight():获取的是控件的总高度
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    public void isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // scrollY == 0是顶部
        // scrollY == offset是底部
        if (scrollY == 0 || scrollY == offset) {
            isMoving = true;
        }
    }

}