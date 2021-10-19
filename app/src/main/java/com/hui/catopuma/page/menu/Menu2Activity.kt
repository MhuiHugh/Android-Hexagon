package com.hui.catopuma.page.menu

import android.view.View
import com.hjq.toast.ToastUtils
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Config
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.DataStoreUtils
import com.hui.core.widget.circle.CircleButton
import com.hui.core.widget.circle.CircleRelativeLayout
import com.hui.core.widget.circle.CircleSixLayout

class Menu2Activity : BaseActivity(), View.OnClickListener {

    lateinit var mViewModel: NoViewModel
    lateinit var click: Menu2Activity.ClickProxy

    //拖动菜单组，根布局处理滑动分发，菜单组拖动位置排列
    private lateinit var circleRl: CircleRelativeLayout
    private lateinit var circleSl: CircleSixLayout

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(NoViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_menu2, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        if (null != getBinding()?.root) {
            //菜单组
            circleRl = getBinding()?.root?.findViewById(R.id.circle_root_crl) as CircleRelativeLayout
            circleSl = getBinding()?.root!!.findViewById(R.id.circle_menu_csl)
            //显示隐藏监听
            circleRl?.setOnMoveListenerListener(circleSl)
            //布局不全屏时设置Click监听，否则可能无法监听OnTouch事件
            circleRl?.setOnClickListener(this)
            val count = circleSl.childCount
            for (i in 0 until count) {
                val child = circleSl.getChildAt(i) as CircleButton
                child.setOnClickListener(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        circleSl.onDirectionChanged(circleRl,DataStoreUtils.readBooleanData(CoreConfig.STORE_CIRCLE_RIGHT,true))
    }

    //-----------------------------------
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.circle_zero_cbtn -> ToastUtils.show(getText(R.string.core_test))
            R.id.circle_back_cbtn->  {finishActivity()}
        }
    }

    /**
     * 点击事件
     */
    inner class ClickProxy {

        fun clickTest(view: View?) {

        }

    }
}