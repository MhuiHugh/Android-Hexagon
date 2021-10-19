package com.hui.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.hui.core.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 横向滚动下标指示适配器
 * Created by Hugh on 6/8/2015.
 */
public class HorizontalScrollIndicatorView extends HorizontalScrollView implements
        View.OnClickListener {

    private final String TAG=this.getClass().getSimpleName();
    private Context mContext;
    /**
     * 视图改变监听
     */
    private CurrentViewChangeListener mCurrentViewChangeListener;

    /**
     * Item单击监听
     */
    private OnItemClickListener mOnClickListener;

    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;

    /**
     * 子元素的宽度
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 当前最后一张图片的index
     */
    private int mCurrentIndex;
    /**
     * 当前第一张图片的下标
     */
    private int mFristIndex;

    /**
     * 数据适配器
     */
    private HorizontalScrollIndicatorAdapter mAdapter;
    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen;
    /**
     * 屏幕的宽度
     */
    private int mScreenWitdh;

    /**
     * 保存View与位置的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

    public HorizontalScrollIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG,"HorizontalScrollIndicatorView()");
        mContext=context;
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWitdh = outMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                // 如果当前scrollX为view的宽度，加载下一张，移除第一张
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                // 如果当前scrollX = 0， 往前设置一张，移除最后一张
                if (scrollX == 0) {
                    loadPreImg();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                mContainer.getChildAt(i).findViewById(R.id.collage_item_colorselect).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            }
            mAdapter.setCurrentIndex(mViewPos.get(view));
            view.findViewById(R.id.collage_item_colorselect).setBackgroundColor(mContext.getResources().getColor(R.color.color_999));
            mOnClickListener.onClick(view, mViewPos.get(view));
        }
    }

    //-----------------------------------------------
    /**
     * 加载下一个视图
     */
    protected void loadNextImg() {
        if(null==mAdapter){
            return;
        }
        // 数组边界值计算
        if (mCurrentIndex == mAdapter.getCount() - 1) {
            return;
        }
        // 移除第一张图片，且将水平滚动位置置0
        scrollTo(0, 0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);

        // 获取下一张图片，并且设置onclick事件，且加入容器中
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);

        // 当前第一张图片小标
        mFristIndex++;
        // 如果设置了滚动监听则触发
        if (mCurrentViewChangeListener != null) {
            notifyCurrentImgChanged();
        }
    }

    /**
     * 加载上个视图
     */
    protected void loadPreImg() {
        // 如果当前已经是第一张，则返回
        if (mFristIndex == 0)
            return;
        // 获得当前应该显示为第一张图片的下标
        int index = mCurrentIndex - mCountOneScreen;
        if (index >= 0) {
            // mContainer = (LinearLayout) getChildAt(0);
            // 移除最后一张
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            // 将此View放入第一个位置
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            // 水平滚动位置向左移动view的宽度个像素
            scrollTo(mChildWidth, 0);
            // 当前位置--，当前第一个显示的下标--
            mCurrentIndex--;
            mFristIndex--;
            // 回调
            if (mCurrentViewChangeListener != null) {
                notifyCurrentImgChanged();

            }
        }
    }
    /**
     * 滑动时的回调
     */
    public void notifyCurrentImgChanged() {
        /**
         * 空接口方法在这里使用时，只需要传必要的参数，而在外面调用时才告诉这个view要如何操作
         */
        mCurrentViewChangeListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));
    }

    /**
     * 加载第一屏的View
     * @param mCountOneScreen
     */
    public void initFirstScreenChildren(int mCountOneScreen) {
        Log.v(TAG,"initFirstScreenChildren()");
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        for (int i = 0; i < mCountOneScreen; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }
        if (mCurrentViewChangeListener != null) {
            notifyCurrentImgChanged();
        }
    }

    //-------------------
    /**
     * 初始化数据，设置数据适配器
     *
     * @param mAdapter
     */
    public void setAdapter(HorizontalScrollIndicatorAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        // 获得适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
        mContainer.addView(view);

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0) {
            int w = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();
            mChildHeight = view.getMeasuredHeight();
            // 计算每次加载多少个View
            mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? mScreenWitdh
                    / mChildWidth + 1 : mScreenWitdh / mChildWidth + 2;
        }
        // 初始化第一屏幕的元素
        initFirstScreenChildren(mCountOneScreen);
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setCurrentImageChangeListener(
            CurrentViewChangeListener mCurrentViewChangeListener) {
        this.mCurrentViewChangeListener = mCurrentViewChangeListener;
    }

    //--------------------------------------------
    /**
     * 滚动时的回调接口
     *
     * 这个接口是自己定义的，先定义好，在调用处再实例化重写它，这里定好一个是位置参数，一个是操作哪处view控件
     */
    public interface CurrentViewChangeListener {
        void onCurrentImgChanged(int position, View viewIndicator);
    }

    /**
     * 条目点击时的回调
     *
     */
    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }
}
