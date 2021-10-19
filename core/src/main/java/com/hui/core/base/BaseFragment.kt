package com.hui.core.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.AppUtils
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog

/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/3/16 14:41
 *    desc   :
 */
abstract class BaseFragment : Fragment() {

    private var mActivity: AppCompatActivity? = null
    private var mBinding: ViewDataBinding? = null

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    /**
     * 是否首次显示
     */
    @Volatile
    private var firstShow = true

    //------系统方法重写-------------------
    override fun onAttach(context: Context) {
        super.onAttach(context)
        CoreLog.v(CoreConfig.TAG,"onAttach()")
        mActivity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CoreLog.v(CoreConfig.TAG," onCreateView()")
        initViewModel()
        val dataBindingConfig = initGetDataBindingConfig()
        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        mBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            dataBindingConfig!!.layout,
            container,
            false
        )

        mBinding?.lifecycleOwner = this
        mBinding?.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)

        val bindingParams: SparseArray<Any?> = dataBindingConfig.getBindingParams()
        if (bindingParams.size() > 0) {
            for (index in 0 until bindingParams.size()) {
                mBinding?.setVariable(bindingParams.keyAt(index), bindingParams.valueAt(index))
            }
        }
      return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoreLog.v(CoreConfig.TAG," onViewCreated()")
        this.init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CoreLog.v(CoreConfig.TAG," onActivityCreated()")
    }

    override fun onResume() {
        super.onResume()
        CoreLog.v(CoreConfig.TAG," onResume()")
    }

    override fun onPause() {
        super.onPause()
        CoreLog.v(CoreConfig.TAG," onPause()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CoreLog.v(CoreConfig.TAG," onDestroyView()")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        CoreLog.v(CoreConfig.TAG," onHiddenChanged() $hidden $firstShow")
        if (firstShow && !hidden) {
            firstShow = false
            firstShow()
        }
    }

    /**
     * 使用ViewPager时，有缓存不调用onHiddenChanged解决
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        CoreLog.v(CoreConfig.TAG," setUserVisibleHint() $isVisibleToUser $firstShow")
        if (firstShow && isVisibleToUser) {
            firstShow = false
            firstShow()
        }
    }

    //-------------------------------
    protected abstract fun initViewModel()

    protected abstract fun initGetDataBindingConfig(): DataBindingConfig?

    //-------------子类可重写方法------------------

    /**
     * onViewCreated中调用
     */
    open fun init(){

    }

    /**
     * 首次显示
     */
    open fun firstShow() {

    }

    /**
     * Fragment的名称，用于友盟页面统计
     *
     * @return
     */
    open fun fragmentName(): String? {
        return javaClass.simpleName
    }

    //-------------------------------

    //TODO tip 1: DataBinding 严格模式（详见 DataBindingFragment - - - - - ）：
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
    // 通过这样的方式，来彻底解决 视图调用的一致性问题，
    // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。

    // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

    //TODO tip 2: Jetpack 通过 "工厂模式" 来实现 ViewModel 的作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域的 Provider 获得的 ViewModel 实例不是同一个，
    //所以如果 ViewModel 对状态信息的保留不符合预期，可以从这个角度出发去排查 是否眼前的 ViewModel 实例不是目标实例所致。

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840

    //TODO tip 1: DataBinding 严格模式（详见 DataBindingFragment - - - - - ）：
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
    // 通过这样的方式，来彻底解决 视图调用的一致性问题，
    // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
    // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
    //TODO tip 2: Jetpack 通过 "工厂模式" 来实现 ViewModel 的作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域的 Provider 获得的 ViewModel 实例不是同一个，
    //所以如果 ViewModel 对状态信息的保留不符合预期，可以从这个角度出发去排查 是否眼前的 ViewModel 实例不是目标实例所致。
    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840
    protected open fun <T : ViewModel?> getFragmentScopeViewModel(modelClass: Class<T>): T {
        if (mFragmentProvider == null) {
            mFragmentProvider = ViewModelProvider(this)
        }
        return mFragmentProvider!!.get(modelClass)
    }

    protected open fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(mActivity!!)
        }
        return mActivityProvider!!.get(modelClass)
    }

    protected open fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(
                (mActivity!!.applicationContext as BaseApplication),
                getApplicationFactory(mActivity!!)!!
            )
        }
        return mApplicationProvider!!.get(modelClass)
    }

      private fun getApplicationFactory(activity: Activity): ViewModelProvider.Factory? {
        checkActivity(this)
        val application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

    private fun checkActivity(fragment: Fragment) {
        val activity = fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    /**
     * TODO tip: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     * 目前的方案是在 debug 模式下，对获取实例的情况给予提示。
     *
     *
     * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     *
     * @return binding
     */
    protected open fun getBinding(): ViewDataBinding? {
        if (AppUtils.isAppDebug() && mBinding != null) {
            CoreLog.w(CoreConfig.TAG, getString(com.hui.core.R.string.core_databinding_warning))
        }
        return mBinding
    }

}