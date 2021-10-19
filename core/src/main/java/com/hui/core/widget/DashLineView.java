package com.hui.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.hui.core.R;

/**
 * Created by Alex on 2016/5/25.
 * https://github.com/Alex-Cin/DashLineView
 * 虚线的高度就是 控件本身的高度，默认 2dp； 虚线 宽度 可以修改； 虚线 颜色可以修改； 虚线之间间距可以修改；
 */
public class DashLineView extends View {
    private Paint paint;
    private Path path;
    /**
     * 虚线的 宽度 - 长度
     */
    private float dashWidth;

    /**
     * 虚线的 高度
     */
    private float dashHeight;

    /**
     * 虚线的颜色
     */
    private int dashColor;
    /**
     * 虚线之间的 间距
     */
    private float dashGap;

    public DashLineView(Context context) {
        super(context);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(widthMeasureSpec);
        int height = measureSize(heightMeasureSpec);
        //dashHeight=height*2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);//设置画笔style空心
        paint.setColor(dashColor);
        paint.setStrokeWidth(dashHeight);//设置画笔宽度
        path.moveTo(dashGap / 2, 0);
        path.lineTo(getWidth(), 0);
        PathEffect effects = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typeA = context.obtainStyledAttributes(attrs,
                R.styleable.DashLineView);
        dashColor = typeA.getColor(R.styleable.DashLineView_dashLineColor, Color.parseColor("#333333"));
        dashWidth = typeA.getDimensionPixelSize(R.styleable.DashLineView_dashLineWidth, 10);
        dashHeight = typeA.getDimensionPixelSize(R.styleable.DashLineView_dashLineHeight, 2);
        dashGap = typeA.getDimensionPixelSize(R.styleable.DashLineView_dashLineSpace, (int) dashWidth);

        //释放资源
        typeA.recycle();
        paint = new Paint();
        path = new Path();
    }

    /**
     * 虚线的 宽度 - 长度
     *
     * @param dashWidth 单位 dp
     */
    public void setDashWidth(float dashWidth) {
        this.dashWidth = ConvertUtils.dp2px(dashWidth);
        invalidate();
    }

    /**
     * 设置虚线的 颜色
     */
    public void setDashColor(int dashColor) {
        this.dashColor = dashColor;
        paint.setColor(dashColor);
        invalidate();
    }

    /**
     * 设置虚线之间的间距
     *
     * @param dashGap 单位 dp
     */
    public void setDashGap(float dashGap) {
        this.dashGap = ConvertUtils.dp2px(dashGap);
        invalidate();
    }

    /**
     * 测量宽和高，这一块可以是一个模板代码(Android群英传)
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureSize(int widthMeasureSpec) {
        int result = 0;
        //从MeasureSpec对象中提取出来具体的测量模式和大小
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            //测量的模式，精确
            result = size;
        } else {
            result= ConvertUtils.dp2px(2.0f);
        }
        return result;
    }


}