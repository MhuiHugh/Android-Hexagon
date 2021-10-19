package com.hui.core.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * 横行有果冻效果ScrollView
 * Created by Menghui on 2017/2/20.
 */
public class JellyHorizontalScrollView extends HorizontalScrollView {

    private View inner;// 子View

    private float x;// 点击时x坐标

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isCount = false;// 是否开始计算

    private boolean isMoving = false;// 是否开始移动.

    private int left;// 拖动时左边距离

    private int mTouchSlop;//系统最少滑动距离

    public JellyHorizontalScrollView(Context context) {
        super(context);
    }

    public JellyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JellyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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
     *
     * @param ev
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (inner != null) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    x = ev.getX();
                    left = 0;
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
                    final float preX = x;// 按下时的y坐标
                    float nowX = ev.getX();// 每时刻y坐标
                    int deltaX = (int) (nowX - preX);// 滑动距离
                    if (!isCount) {
                        deltaX = 0; // 在这里要归0.
                    }
                    //滑动距离小于系统最小滑动&&
                    if (Math.abs(deltaX) < mTouchSlop && left <= 0) {
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
                        inner.layout(inner.getLeft() + deltaX / 3, inner.getTop(), inner.getRight() + deltaX / 3, inner.getBottom());
                        left += (deltaX / 6);
                    }
                    isCount = true;
                    x = nowX;
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
        TranslateAnimation ta = new TranslateAnimation(inner.getLeft(), 0, 0, 0);
        ta.setDuration(200);
        //设置动画插值器
        ta.setInterpolator(new DecelerateInterpolator());
        inner.startAnimation(ta);

        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.left, normal.right, normal.bottom);
        normal.setEmpty();

        // 手指松开要归0.
        isCount = false;
        x = 0;
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
        int offset = inner.getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        // scrollY == 0是顶部
        // scrollY == offset是底部
        if (scrollX == 0 || scrollX == offset) {
            isMoving = true;
        }
    }

}