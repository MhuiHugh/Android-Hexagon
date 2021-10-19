package com.hui.catopuma.page.input

import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel

/**
 * 输入框
 */
class InputActivity : BaseActivity() {

    lateinit var mViewModel: NoViewModel
    lateinit var click: InputActivity.ClickProxy

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(NoViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_input, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {

    }

    /**
     * 点击事件
     */
    inner class ClickProxy {

        fun clickTest() {

        }

    }

}