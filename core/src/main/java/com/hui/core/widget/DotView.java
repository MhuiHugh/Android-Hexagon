package com.hui.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.hui.core.R;

/**
 * 绘制半径和颜色可定制的单色圆
 * Created by Mhui on 2016/6/7.
 */
public class DotView extends View {

    private Context mContext;
    //定义、并创建画笔
    Paint paint = new Paint();
    int color = 0;
    float radius = 0;

    public DotView(Context context) {
       this(context,null);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        TypedArray typeA = context.obtainStyledAttributes(attrs,
                R.styleable.DotView);
        color = typeA.getInteger(R.styleable.DotView_dotColor, Color.RED);
        radius = typeA.getDimension(R.styleable.DotView_dotRadiusw, 10.0f);
        //LogUtil.v("radius:"+radius);
        typeA.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        paint.setAntiAlias(true); //消除锯齿
        //设置画笔的颜色
        paint.setColor(color);
        //绘制一个小圆点
        canvas.drawCircle(centerX, centerY, ConvertUtils.px2dp(radius), paint);
    }

    /**
     * 设置圆的颜色并重新绘制
     *
     * @param newcolor
     */
    public void setDotColor(int newcolor) {
        color = newcolor;
        //重新布局
        //requestLayout();
        //重绘自己
        invalidate();
    }

}
