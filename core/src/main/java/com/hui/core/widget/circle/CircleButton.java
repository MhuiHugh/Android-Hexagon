package com.hui.core.widget.circle;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;

import com.hui.core.R;

public class CircleButton extends AppCompatImageView {

    private final String TAG = this.getClass().getSimpleName();

    //点击时外圈颜色是背景色的%
    private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
    //外圈的透明度
    private static final int PRESSED_RING_ALPHA = 60;
    //默认外圈宽度
    private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
    //动画持续时间
    private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

    private int centerY;//中心x
    private int centerX;//中心y
    private int outerRadius;//外圆半径
    private int pressedRingRadius;//按下时

    private Paint circlePaint;//未按下背景画笔
    private Paint focusPaint;//按下画笔
    private Paint textPaint;//文本内容画笔

    private float animationProgress;//外圈动画

    private int pressedRingWidth;//按下时圆环宽度，根据设备像素密度
    private int defaultColor = Color.BLACK;//默认圆颜色
    private int pressedColor;//按下时颜色
    private ObjectAnimator pressedAnimator;
    private String text = "";//字体
    private int textColor = Color.BLUE;//字体颜色
    private float textSizePx = 24;//默认字体大小Px

    //---------------------------------------拖动相关
    private Context context;//上下文
    private int screenWidth, screenHeight;//屏幕宽高
    private int directionChangeMin = 0;//方向改变最小滑动距离为屏幕宽度3/2
    private int directionX = 0;//每次滑动初始X值
    private int lastX, lastY;//最后x,y
    private int downX, downY;//按下时x,y
    private int width = -1, height = -1;//View本身宽高
    private int statusBarHeight = -1;//状态栏高度
    private int initialY = 0;//初始Y轴，松开后恢复初始Y坐标
    private boolean isMove;//移动?点击
    private FrameLayout.LayoutParams lp;
    private OnDirectionChangedListener listener;//贴边方向切换监听
    private OnClickListener clickListener;//点击监听

    //--------------------------------------------
    public CircleButton(Context context) {
        super(context);
        init(context, null);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    //--------------------------------------------

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        dealInit();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画外圆
        canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
        //画内圆
        canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth, circlePaint);
        //绘制图片，注意绘制层次避免遮挡
        super.onDraw(canvas);
        //画文本
        drawTextInCenter(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        outerRadius = Math.min(w, h) / 2;
        pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
    }

    //--------------------------------------------
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        setTouchPressed(pressed);
    }

    /**
     * 设置点击特效
     *
     * @param pressed
     */
    private void setTouchPressed(boolean pressed) {
        if (circlePaint != null) {
            circlePaint.setColor(pressed ? pressedColor : defaultColor);
            focusPaint.setAlpha(pressed ? PRESSED_RING_ALPHA : 0);
        }
        if (pressed) {
            showPressedRing();
        } else {
            hidePressedRing();
        }
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        this.invalidate();
    }

    /**
     * 显示点击特效
     */
    private void showPressedRing() {
        pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
        pressedAnimator.start();
    }

    /**
     * 隐藏点击特效
     */
    private void hidePressedRing() {
        pressedAnimator.setFloatValues(pressedRingWidth, 0f);
        pressedAnimator.start();
    }

    private void init(Context context, AttributeSet attrs) {
        Log.v(TAG, "init()");
        this.context = context;
        this.setFocusable(true);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusPaint.setStyle(Paint.Style.STROKE);

        pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
                .getDisplayMetrics());

        //默认背景颜色
        int color = Color.GRAY;

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton);
            color = a.getColor(R.styleable.CircleButton_cb_color, color);
            pressedRingWidth = (int) a.getDimension(R.styleable.CircleButton_cb_pressedRingWidth, pressedRingWidth);
            text = a.getString(R.styleable.CircleButton_cb_text);
            textSizePx = a.getDimension(R.styleable.CircleButton_cb_text_size, 24);
            textColor = a.getColor(R.styleable.CircleButton_cb_text_color, Color.BLUE);
            a.recycle();
        }
        setColor(color);
        focusPaint.setStrokeWidth(pressedRingWidth);
        final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
        pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
        pressedAnimator.setDuration(pressedAnimationTime);
        setTextColor(textColor);
        //获取屏幕尺寸信息
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        directionChangeMin = screenWidth / 3 * 2;
        //获取状态栏高度
        if (statusBarHeight == -1) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        dealInit();
    }

    private int getHighlightColor(int color, int amount) {
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }

    //-----------------------------------------------

    /**
     * 设置圆颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.defaultColor = color;
        this.pressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP);
        circlePaint.setColor(defaultColor);
        focusPaint.setColor(defaultColor);
        focusPaint.setAlpha(0);
        this.invalidate();
    }

    /**
     * 设置文本
     *
     * @param str
     */
    public void setText(String str) {
        text = str;
        this.invalidate();
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        if (textPaint == null) {
            textPaint = new Paint();
        }
        //抗锯齿
        textPaint.setAntiAlias(true);
        textPaint.setColor(color);
        textPaint.setTextSize(textSizePx);
        // 画笔水平居中
        textPaint.setTextAlign(Paint.Align.CENTER);
        this.invalidate();
    }

    /**
     * 将文本绘制在视图中间
     *
     * @param canvas
     */
    private void drawTextInCenter(Canvas canvas) {
        if (text == null) {
            Log.w(TAG, "text str is null!");
            return;
        }
        // 计算文字高度,处理垂直居中
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        // 计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = getHeight() - (getHeight() - fontHeight) / 2
                - fontMetrics.bottom;
        canvas.drawText(text, getWidth() / 2, textBaseY, textPaint);
    }

    //-----------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastX = (int) event.getRawX();
                downY = lastY = (int) event.getRawY();
                directionX = downX;
                setMoveStatus(false);
                dealInit();
                break;
            case MotionEvent.ACTION_MOVE:
                //这个周期移动的距离
                int dx = (int) event.getRawX() / 3 - lastX / 3;
                int dy = (int) event.getRawY() / 3 - lastY / 3;

                if (!isMove) {
                    int dx1 = Math.abs((int) event.getRawX() - downX);
                    int dy1 = Math.abs((int) event.getRawY() - downY);
                    if (dx1 > 20 || dy1 > 20) {
                        setMoveStatus(true);
                    }
                }

                if (isMove) {
                    int top = this.getTop() + dy;
                    int left = this.getLeft() + dx;
                    //不能移出屏幕控制
                    if (left < 0) {//左
                        left = 0;
                    }
                    if (top < 0) {//上
                        top = 0;
                    }
                    if (left > screenWidth - width) {//右
                        left = screenWidth - width;
                    }
                    if (top > screenHeight - height - statusBarHeight) {//下
                        top = screenHeight - height - statusBarHeight;
                    }

                    lp.setMargins(left, top, 0, 0);
                    setLayoutParams(lp);
                    //Log.v(TAG, "onTouchEvent() move:x" + left + "-y:" + top);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    //当滑动距离大于屏幕宽度2/3时切换方向
                    if (Math.abs(lastX - directionX) > directionChangeMin) {
                        //单次滑动来回切换处理
                        if (directionX > directionChangeMin) {
                            directionX = 0;
                        } else {
                            directionX = screenWidth;
                        }
                        if (listener != null) {
                            listener.onDirectionChanged(this, directionX == screenWidth);
                        }
                        //从左向右滑动:从右向左滑动
                        int showX = directionX;
                        if (directionX > 0) {
                            showX = screenWidth - left;
                        } else {
                            showX = lastX - width;
                        }
                        lp.setMargins(showX, top, 0, 0);
                        setLayoutParams(lp);
                        //Log.v(TAG, "x,y:" + showX + "  " + top);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (clickListener != null && !isMove && !isLongClickable()) {
                    clickListener.onClick(this);
                }
                int left1 = 0;
                if (directionX < directionChangeMin) {//靠右
                    left1 = 0;
                } else {//靠左
                    left1 = screenWidth - width;
                }
                //封装在ViewGrop中无法拖动了
                lp.setMargins(left1, initialY, 0, 0);
                setLayoutParams(lp);
                setMoveStatus(false);
                break;
        }

        return super.onTouchEvent(event);
    }


    //-------------------------------

    /**
     * 设置初始Y坐标，松开拖动恢复Y
     *
     * @param y Y轴坐标
     */
    public void setInitialXY(int x,int y) {
        dealInit();
        initialY = y;
        if (null != lp) {
            lp.setMargins(x, y, 0, 0);
            setLayoutParams(lp);
        }
    }

    /**
     * 改变贴边方向
     */
    public void directionChange() {
        if (null != lp && screenWidth > 0) {
            if (getLeft() == 0) {//当前靠左切换到右边
                lp.setMargins(screenWidth - width, initialY, 0, 0);
                setLayoutParams(lp);
            } else {//当前靠右切换到左边
                lp.setMargins(0, initialY, 0, 0);
                setLayoutParams(lp);
            }
        }
    }

    /**
     * 控件靠左
     *
     * @return
     */
    public boolean directionLeft() {
        return getLeft() == 0;
    }

    /**
     * 设置移动状态
     *
     * @param status
     */
    private void setMoveStatus(boolean status) {
        isMove = status;
        CircleRelativeLayout.isCircleChildMoved = isMove;
    }

    /**
     * 处理参数初始化
     */
    private void dealInit() {
        //获取View高度
        if (width < 1) {
            width = this.getWidth();
            height = this.getHeight();
        } else {
            return;
        }
        //LayoutParams初始化
        if (width > 1 && null == lp) {
            lp = new FrameLayout.LayoutParams(getWidth(), getHeight());
            setLayoutParams(lp);
        }
    }

    //--------------------------------

    /**
     * 设置贴边方向切换事件监听
     *
     * @param listener
     */
    public void setOnDirectionChangedListener(OnDirectionChangedListener listener) {
        this.listener = listener;
    }

    public void setOnClickListener(OnClickListener listener) {
        clickListener = listener;
    }

    /**
     * 监听拖动贴边方向改变
     * left true
     */
    public interface OnDirectionChangedListener {
        void onDirectionChanged(View view, boolean right);
    }

    /**
     * 单击监听
     */
    public interface OnClickListener {
        void onClick(View view);
    }

}
