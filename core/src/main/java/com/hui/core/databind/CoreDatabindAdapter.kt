package com.hui.core.databind

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils
import com.hui.core.imageloader.ImageLoaderV4
import com.hui.core.imageloader.listener.IImageLoaderListener
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/417:31
 *    desc   :
 */
object CoreDatabindAdapter {

    /**
     * 显示隐藏
     */
    @JvmStatic
    @BindingAdapter(value = ["visible"], requireAll = false)
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 点击防抖
     */
    @JvmStatic
    @BindingAdapter(value = ["onClickWithDebouncing"], requireAll = false)
    fun onClickWithDebouncing(view: View?, clickListener: View.OnClickListener?) {
        ClickUtils.applySingleDebouncing(view, clickListener)
    }

    /**
     * 选中状态
     */
    @JvmStatic
    @BindingAdapter(value = ["selected"], requireAll = false)
    fun selected(view: View, select: Boolean) {
        view.isSelected = select
    }

    //-------------------图片加载-------------------------------

    /**
     * ImageView 设置drawable
     */
    @JvmStatic
    @BindingAdapter(value = ["imageRes"], requireAll = false)
    fun setImageRes(imageView: ImageView, imageRes: Int) {
        imageView.setImageResource(imageRes)
    }

    /**
     * ImageView 加载图片
     */
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "imagePlaceHolder","imageErrorHolder","imageLoaderListener","imageCacheInMemory"], requireAll = false)
    fun loadUrl(view: ImageView, imageUrl: String?, imagePlaceHolder: Int?, imageErrorHolder:Int?, imageLoaderListener: IImageLoaderListener?, imageCacheInMemory: Boolean? = false) {
        CoreLog.v(CoreConfig.TAG,"$view  $imageUrl $imagePlaceHolder $imageErrorHolder  $imageCacheInMemory")
        ImageLoaderV4.getInstance().displayImage(view.context,imageUrl,view, imagePlaceHolder?:0, imageErrorHolder?:0,imageLoaderListener,imageCacheInMemory?:false)
    }


}