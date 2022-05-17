package com.hui.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.hui.core.R
import com.hui.core.utils.LogUtils
import kotlin.math.abs
import kotlin.math.sqrt

class CircleZoomView : FrameLayout, View.OnTouchListener {

    private val TAG = this.javaClass.simpleName
    lateinit var mContext: Context

    //最后x,y
    private var lastX = -1f
    private var lastY = -1f

    //按下时x,y
    private var downX = 0f
    private var downY = 0f
    private val minMove = 10f
    private val ringWidth = 20f
    private var mZoom = false
    private lateinit var lp: ViewGroup.LayoutParams

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (null == event) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX
                downX = lastX
                lastY = event.rawY
                downY = lastY

                mZoom = false
                if (null != this.layoutParams) {
                    lp = this.layoutParams
                    LogUtils.v("$TAG LayoutParams初始参数 ${lp.width} ${lp.height} ")
                }
                LogUtils.v("$TAG down w $width $height  ${pivotX}  $pivotY ")
            }
            MotionEvent.ACTION_MOVE -> {
                if (!mZoom) {
                    val dx1 = abs(event.rawX.toInt() - downX).toInt()
                    val dy1 = abs(event.rawY.toInt() - downY).toInt()
                    if (dx1 > minMove || dy1 > minMove) {
                        mZoom = true
                    }
                }

                if (mZoom) {
                    val dx = event.rawX - lastX
                    val dy = event.rawY - lastY
                    val size = (lp.width + dx).toInt()
                    lp.width = size
                    lp.height = size
                    layoutParams = lp
                    LogUtils.v("$TAG 缩放处理 $dx $dy ${lp.width} ${lp.height}")
                }
                lastX = event.rawX
                lastY = event.rawY
                //LogUtils.v("$TAG $lastX $lastY  $width $height $pivotX $pivotY")
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mZoom = false
            }
        }
        return true
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LogUtils.v("$TAG init()")
        mContext = context
        val rootView = LayoutInflater.from(context)
            .inflate(R.layout.widget_circle_zoom_view, this, true)
        val dotView = rootView.findViewById<DotView>(R.id.dotView)
        dotView.setOnTouchListener(this)
    }

    /**
     * 判断是否在圆环内
     */
    private fun judgeZoom(x: Float, y: Float): Boolean {
        val diameter: Float = (width).toFloat()
        val a: Float = abs(x - diameter / 2)
        val b: Float = abs(y - diameter / 2)
        //勾股定律求斜边长度
        val c = sqrt(a * a + b * b)
        //等边直角三角形斜边长度
        val d = sqrt(2 * ringWidth)
        //圆环内滑动判断
        if (c > diameter / 2 || c < d) {
            LogUtils.v("$TAG 未在圆环内！");
            return false
        } else {
            LogUtils.v("$TAG 在圆环内！");
            return true
        }
    }

}


