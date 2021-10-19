package com.hui.catopuma.page.hexagon

import android.graphics.Color
import android.view.View
import com.hjq.toast.ToastUtils
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.page.ActionFragmentActivity
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel
import com.hui.core.utils.LogUtils
import com.hui.core.widget.HorizontalScrollIndicatorAdapter
import com.hui.core.widget.HorizontalScrollIndicatorView
import java.util.*
import kotlin.collections.ArrayList

/**
 * 展示旋转六边形菜单组件
 */
class HexagonActivity : BaseActivity() {

    lateinit var mViewModel: NoViewModel
    lateinit var click: HexagonActivity.ClickProxy

    //颜色选择器
    private lateinit var horizontalScrollIndicatorView: HorizontalScrollIndicatorView

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(NoViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_hexagon, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        //颜色选择器
        if (null != getBinding()?.root) {
            val colors: MutableList<Int> = ArrayList()
            val random = Random(2)
            for (i in 0..49) {
                colors.add(Color.argb(255, 50 * i, random.nextInt(255), random.nextInt(255)))
            }

            horizontalScrollIndicatorView = getBinding()?.root!!.findViewById(R.id.hexagon_hsi)
            horizontalScrollIndicatorView.setAdapter(HorizontalScrollIndicatorAdapter(this, colors))
            horizontalScrollIndicatorView.setOnItemClickListener { view: View, i: Int ->
                ToastUtils.show(resources.getString(R.string.home_color_select, i))
            }
        }
    }

    /**
     * 点击事件
     */
    inner class ClickProxy {

        fun clickTest() {

        }

        fun clickHexagonTest() {
            LogUtils.v("clickHexagonTest")

            ToastUtils.show(getString(R.string.core_test))
        }

        fun clickHexagonCar() {
            ToastUtils.show("Car")
        }

    }
}