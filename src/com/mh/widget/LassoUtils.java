package com.mh.widget;
import android.graphics.PointF;
import android.util.Log;

import java.util.List;

/**
 * 核心判断，一个点是否在外凸多边形内
 */
public class LassoUtils {
    private final String TAG = this.getClass().getSimpleName();
    //饿汉单例模式
    private  static final LassoUtils instance=new LassoUtils();
    // 多边形各个点坐标
    private float[] mPolyX, mPolyY;
    // 有几个点
    private int mPolySize;

    private LassoUtils(){
        Log.v(TAG, "LassoUtils()");
    }

    public static LassoUtils getInstance(){
        return instance;
    }

    /**
     * 构造
     * @param pointFs 多边形路径
     */
    public void setLassoList(List<PointF> pointFs) {
        this.mPolySize = pointFs.size();
        this.mPolyX = new float[this.mPolySize];
        this.mPolyY = new float[this.mPolySize];

        for (int i = 0; i < this.mPolySize; i++) {
            this.mPolyX[i] = pointFs.get(i).x;
            this.mPolyY[i] = pointFs.get(i).y;
        }
    }

    /**
     * 射线法判断点是否在多边形内部
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y) {
        boolean result = false;
        for (int i = 0, j = mPolySize - 1; i < mPolySize; j = i++) {
            if ((mPolyY[i] < y && mPolyY[j] >= y)
                    || (mPolyY[j] < y && mPolyY[i] >= y)) {
                if (mPolyX[i] + (y - mPolyY[i]) / (mPolyY[j] - mPolyY[i])
                        * (mPolyX[j] - mPolyX[i]) < x) {
                    result = !result;
                }
            }
        }
        return result;
    }

}