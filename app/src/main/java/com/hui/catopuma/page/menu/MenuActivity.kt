package com.hui.catopuma.page.menu

import android.content.Intent
import android.view.View
import com.hjq.toast.ToastUtils
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Config
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.DataStoreUtils
import com.hui.core.utils.LogUtils
import com.hui.core.widget.circle.CircleButton
import com.hui.core.widget.circle.CircleRelativeLayout
import com.hui.core.widget.circle.CircleSixLayout

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/10/8 15:40
 *    desc   :
 */
class MenuActivity : BaseActivity(),View.OnClickListener {

    lateinit var mViewModel: MenuViewModel
    lateinit var click: MenuActivity.ClickProxy

    //拖动菜单组，根布局处理滑动分发，菜单组拖动位置排列
    private lateinit var circleRl: CircleRelativeLayout
    private lateinit var circleSl: CircleSixLayout

    override fun initViewModel() {
      mViewModel=getActivityScopeViewModel(MenuViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_menu, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        LogUtils.v("initInCreate()")
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
            //记录位置
            //circleRl.setOnToRightListener { right->   }
        }
    }

    override fun onResume() {
        super.onResume()
        var mRight=DataStoreUtils.readBooleanData(CoreConfig.STORE_CIRCLE_RIGHT,true)
        LogUtils.v("onResume() $mRight")

        circleSl.onDirectionChanged(circleRl,mRight)
    }

    //-------------------------------------

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.circle_zero_cbtn -> {
                ToastUtils.show("zero")
                val intent= Intent(this, Menu2Activity::class.java)
                startActivity(intent)
            }
            R.id.circle_one_cbtn ->  ToastUtils.show("one")
            R.id.circle_two_cbtn ->  ToastUtils.show("two")
            R.id.circle_back_cbtn->  {finishActivity()}
        }
    }

    /**
     * 点击事件
     */
    inner class ClickProxy {

        fun clickTest() {
         ToastUtils.show(R.string.core_test)
        }

    }

    //--------------------------------

}