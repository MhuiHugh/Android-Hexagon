package com.hui.catopuma.page.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Constant
import com.hui.catopuma.page.hexagon.HexagonActivity
import com.hui.catopuma.page.input.InputActivity
import com.hui.catopuma.page.menu.MenuActivity
import com.hui.catopuma.page.other.CircleZoomActivity
import com.hui.catopuma.page.web.WebActivity
import com.hui.core.base.BaseFragment
import com.hui.core.base.DataBindingConfig
import com.hui.core.base.NoViewModel
import com.hui.core.utils.LogUtils

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1716:16
 *    desc   :
 */
class HomeFragment : BaseFragment() {

    lateinit var mViewModel: NoViewModel
    lateinit var mainVM: MainViewModel

    lateinit var click: ClickProxy

    //------------------

    override fun initViewModel() {
        LogUtils.v("${Constant.TAG} initViewModel()")
        mViewModel = getFragmentScopeViewModel(NoViewModel::class.java)
        mainVM = getActivityScopeViewModel(MainViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        LogUtils.v("${Constant.TAG} initGetDataBindingConfig()")
        click = ClickProxy()
        return DataBindingConfig(R.layout.fragment_home, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstShow()
    }

    override fun firstShow() {
        super.firstShow()
        LogUtils.v("${Constant.TAG} firstShow()")
        //跳转到菜单组
        getBinding()?.root?.findViewById<Button>(R.id.home_menu_btn)?.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    //--------------------------------

    //--------------------------------
    /**
     * 点击事件
     */
    inner class ClickProxy {
        fun clickTest() {

        }

        fun clickStartWeb() {
            LogUtils.v("clickStartWeb() ")
            WebActivity.startWebActivity(
                context,
                "https://www.baidu.com/",
                action = WebActivity.WEB_PAGE
            )
        }

        fun clickStartWebPdf() {
            val pdfUrl =
                "https://raw.githubusercontent.com/MhuiHugh/Resource/master/File/Flutter.pdf"
            WebActivity.startWebActivity(
                context,
                pdfUrl,
                action = WebActivity.WEB_FILE,
                title = "文件显示"
            )
        }

        fun clickGoHexagon() {
            val intent = Intent(context, HexagonActivity::class.java)
            startActivity(intent)
        }

        fun clickInput() {
            val intent = Intent(context, InputActivity::class.java)
            startActivity(intent)
        }

        fun clickStartCircleZoom() {
            val intent = Intent(context, CircleZoomActivity::class.java)
            startActivity(intent)
        }
    }

}