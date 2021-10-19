package com.hui.catopuma.page.home

import android.view.View
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Constant
import com.hui.core.base.BaseFragment
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel
import com.hui.core.utils.LogUtils

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1716:20
 *    desc   :
 */
class PersonalFragment:BaseFragment() {

    lateinit var mViewModel: NoViewModel
    lateinit var click: ClickProxy

    override fun initViewModel() {
        mViewModel=getFragmentScopeViewModel(NoViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.fragment_personal, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun firstShow() {
        super.firstShow()
        LogUtils.v("${Constant.TAG}  firstShow()")
    }

    /**
     * 点击事件
     */
    inner class ClickProxy{
        fun clickTest(view: View?){

        }
    }

}