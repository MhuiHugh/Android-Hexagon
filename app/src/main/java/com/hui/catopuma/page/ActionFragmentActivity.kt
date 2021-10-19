package com.hui.catopuma.page

import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Constant
import com.hui.catopuma.page.home.HomeFragment
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel
import com.hui.core.utils.LogUtils

class ActionFragmentActivity : BaseActivity(){

    lateinit var mViewModel: NoViewModel
    lateinit var click:ClickProxy

    override fun initViewModel() {
        mViewModel=getActivityScopeViewModel(NoViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_action, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        LogUtils.v("${Constant.TAG} initInCreate()")
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val homeFragment=HomeFragment()
        transaction.replace(R.id.action_fl,homeFragment).commitNow()
    }

    /**
     * 点击事件
     */
    inner class ClickProxy{
        fun clickTest(){

        }
    }
}