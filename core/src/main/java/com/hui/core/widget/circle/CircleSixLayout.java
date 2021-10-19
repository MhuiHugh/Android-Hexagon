package com.hui.core.widget.circle;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.hui.core.R;
import com.hui.core.utils.CoreConfig;
import com.hui.core.utils.CoreLog;
import com.hui.core.utils.DataStoreUtils;

/**
 * 菜单按钮封装，最多显示6个按钮，全屏显示，子控件宽度高度相等
 * 按钮组拖动切换方向，按钮组显示隐藏，单个按钮宽高位置
 * Created by Menghui on 2016/8/17.
 */
public class CircleSixLayout extends FrameLayout implements CircleButton.OnDirectionChangedListener, CircleRelativeLayout.OnMoveListener {

    private final String TAG = this.getClass().getSimpleName();
    private Context con;
    private int screenWidth, screenHeight;//屏幕宽高
    private boolean isFirstDraw = true;//第一次绘制前初始位置

    //属性值
    private int paddingBottom = 0;//距离屏幕底部距离
    private int itemMargin = 0;//子控件间距
    private int itemWidth = 0;//子控件宽度&高度
    private boolean isRight = false;//靠右显示

    public CircleSixLayout(Context context) {
        super(context);
    }

    public CircleSixLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleSixLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleSixLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.v(TAG,"onLayout()");
        if (isFirstDraw) {
            dealLayout();
            isFirstDraw = false;
        }
    }

    /**
     * 当View被附着在窗口时触发
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v(TAG, "onAttachedToWindow()");
    }

    //-----事件监听----------

    /**
     * 父布局左滑
     */
    @Override
    public void toLeft() {
        Log.v(TAG, "toLeft()" + isRight);
        if (isRight) {//靠右
            if (this.getVisibility() == GONE) {
                this.setAnimation(AnimationUtils.makeInAnimation(con, false));
                this.setVisibility(View.VISIBLE);
            }
        } else {
            if (this.getVisibility() == VISIBLE) {
                this.setAnimation(AnimationUtils.makeOutAnimation(con, false));
                this.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 父布局右滑
     */
    @Override
    public void toRight() {
        Log.v(TAG, "toRight()" + isRight);
        if (isRight) {//靠右
            if (this.getVisibility() == VISIBLE) {
                this.setAnimation(AnimationUtils.makeOutAnimation(con, true));
                this.setVisibility(View.GONE);
            }
        } else {
            if (this.getVisibility() == GONE) {
                this.setAnimation(AnimationUtils.makeInAnimation(con, true));
                this.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示或隐藏Menu
     *
     * @param type
     */
    public void showHideMenu(int type) {
        if (type == View.VISIBLE) {
            if (this.getVisibility() == GONE) {
                this.setAnimation(AnimationUtils.makeInAnimation(con, false));
                this.setVisibility(View.VISIBLE);
            }
        } else if (type == View.GONE) {
            if (this.getVisibility() == VISIBLE) {
                this.setAnimation(AnimationUtils.makeOutAnimation(con, true));
                this.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 子控件方向切换
     *
     * @param view
     * @param right 靠右true
     */
    @Override
    public void onDirectionChanged(View view, boolean right) {
        //CoreLog.v(CoreConfig.TAG,"onDirectionChanged() "+right);
        DataStoreUtils.INSTANCE.putSyncData(CoreConfig.STORE_CIRCLE_RIGHT,right);

        if(isRight!=right) {
            isRight = right;
            final int count = getChildCount();
            //方向切换遍历处理
            for (int i = 0; i < count; i++) {
                final CircleButton child = (CircleButton) getChildAt(i);
                if (view.getId() != child.getId()) {
                    child.directionChange();
                }
            }
        }
    }

    //-----------------------
    private void init(Context context, AttributeSet attrs) {
        con = context;
        TypedArray typeA = context.obtainStyledAttributes(attrs,
                R.styleable.CircleSixLayout);
        paddingBottom = typeA.getDimensionPixelOffset(R.styleable.CircleSixLayout_cs_padding_bottom, 0);
        itemMargin = typeA.getDimensionPixelOffset(R.styleable.CircleSixLayout_cs_item_margin, 0);
        itemWidth = typeA.getDimensionPixelOffset(R.styleable.CircleSixLayout_cs_item_width, 90);
        isRight = typeA.getBoolean(R.styleable.CircleSixLayout_cs_right, false);
        //方向校准
        isRight=DataStoreUtils.INSTANCE.readBooleanData(CoreConfig.STORE_CIRCLE_RIGHT,true);
        //CoreLog.v(CoreConfig.TAG,"方向校准right:"+isRight);
        typeA.recycle();

        //获取屏幕尺寸信息
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        Log.v(TAG, "init() ScreenW:H " + screenWidth + "-" + screenHeight + "  paddingBottom:" + paddingBottom + " itemMargin:" + itemMargin + " itemWidth:" + itemWidth + " isRight:" + isRight);
    }

    /**
     * 处理控件位置计算排布
     */
    private void dealLayout() {
        final int count = getChildCount();
        //Log.v(TAG, "dealLayout()" + count);
        int countSize = 0;//隐藏的个数
        // 遍历item设置位置
        for (int i = count - 1; i >= 0; i--) {
            final CircleButton child = (CircleButton) getChildAt(i);
            if (child.getVisibility() == GONE) {
                countSize++;
                continue;
            }
            final int w = itemWidth;
            final int h = itemWidth;
            int mleft = 0;
            int mtop = screenHeight - paddingBottom - h - (h + itemMargin) * (count - i - countSize);
            if (isRight) {//靠右
                mleft = screenWidth - w;
            } else {//靠左
                mleft = 0;
            }
            //Log.v(TAG, "dealLayout() left:" + mleft + "--top:" + mtop);
            //左上右下，相对父布局位置
            child.setInitialXY(mleft, mtop);
            child.setOnDirectionChangedListener(this);
        }
    }

}
