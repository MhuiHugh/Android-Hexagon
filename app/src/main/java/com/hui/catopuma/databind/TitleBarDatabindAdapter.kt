package com.hui.catopuma.databind

import androidx.databinding.BindingAdapter
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1214:59
 *    desc   :标题栏 控件
 *    https://github.com/getActivity/TitleBar
 */
object TitleBarDatabindAdapter {

    /**
     * TitleBar常用属性
     */
    @JvmStatic
    @BindingAdapter(value = ["titleBarListerer","titleBarLeftIcon","titleBarTitle","titleBarRightTitle","titleBarRightIcon","titleBarLineVisible"], requireAll = false)
    fun titleBar(view: TitleBar, titleBarListerer: OnTitleBarListener?=null, titleBarLeftIcon:Int=0, titleBarTitle:String?, titleBarRightTitle:String?, titleBarRightIcon:Int=0,titleBarLineVisible:Boolean=true) {
        if(null!=view){
            if(null!=titleBarListerer){
                view.setOnTitleBarListener(titleBarListerer)
            }
            if(titleBarLeftIcon==0){
                // 不显示返回图标
                view.leftIcon = null
            }else{
                view.setLeftIcon(titleBarLeftIcon)
            }
            if(titleBarTitle.isNullOrEmpty()){
                view.title=""
            }else{
                view.title=titleBarTitle
            }
            if(titleBarRightTitle.isNullOrEmpty()){
                view.rightTitle=""
            }else{
                view.rightTitle=titleBarRightTitle
            }
            if(titleBarRightIcon==0){
                view.rightIcon=null
            }else{
                view.setRightIcon(titleBarRightIcon)
            }
            view.setLineVisible(titleBarLineVisible)
        }
    }

}