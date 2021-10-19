package com.hui.catopuma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ClickUtils
import com.hui.catopuma.data.bean.Ad
import com.youth.banner.adapter.BannerAdapter
import com.hui.catopuma.databinding.AdapterStartupBannerBinding
import com.hui.core.imageloader.ImageLoaderV4

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/1010:14
 *    desc   :
 */
class StartupAdapter(datas: MutableList<Ad>?) : BannerAdapter<Ad, StartupAdapter.AdViewHolder>(datas) {

    private var binding: AdapterStartupBannerBinding? = null

    private val typeBannerAd: Int = 100
    private val typeBannerWelcome: Int = 101

    private var listener:OnListenerClickWelcome?=null
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): AdViewHolder {
        binding = AdapterStartupBannerBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return AdViewHolder(binding!!)
    }

    override fun onBindView(holder: AdViewHolder?, data: Ad?, position: Int, size: Int) {
        if (null != holder && null != data && null != binding) {
            when (holder?.itemViewType) {
                typeBannerAd -> {
                    ImageLoaderV4.getInstance().displayImageInResource(binding!!.startupBannerImgv.context,data.imageDrawableId,binding!!.startupBannerImgv)
                    binding!!.startupBannerTv.visibility = View.GONE
                }
                typeBannerWelcome -> {
                    ImageLoaderV4.getInstance().displayImageInResource(binding!!.startupBannerImgv.context,data.imageDrawableId,binding!!.startupBannerImgv)
                    binding!!.startupBannerTv.visibility = View.VISIBLE
                    if(null!=listener) {
                        ClickUtils.applySingleDebouncing(
                            binding!!.startupBannerTv,
                            View.OnClickListener {
                                listener?.clickWelcome()
                            })
                    }
                }
            }
        }
    }

    /**
     * ViewType 返回
     */
    override fun getItemViewType(position: Int): Int {
        return getData(getRealPosition(position)).adType
    }

    public fun setOnListenerClickWelcome(mlistener:OnListenerClickWelcome){
        listener=mlistener
    }

    public interface OnListenerClickWelcome{
        abstract fun clickWelcome()
    }

    class AdViewHolder(binding: AdapterStartupBannerBinding) : RecyclerView.ViewHolder(binding.root)

}