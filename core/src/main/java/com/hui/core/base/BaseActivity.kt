package com.hui.core.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.gyf.immersionbar.ImmersionBar
import com.hui.core.R
import com.hui.core.utils.ActivityCollector
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.coroutines.Job
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager
import java.lang.ref.WeakReference

/**
 * Activity基类
 * @author Mhui
 *状态栏，布局状态切换,loading
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mBinding: ViewDataBinding? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    /**
     * 状态管理
     */
    private var statusLayoutManager: StatusLayoutManager? = null

    private var weakRefActivity: WeakReference<Activity>? = null

    /**
     * 加载loading
     */
    private lateinit var loadingPopup:LoadingPopupView

    //-------生命周期方法----------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoreLog.v(CoreConfig.TAG,"onCreate()")
        initBeforeSetContentView()
        initBase()
        initInCreate()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        CoreLog.v(CoreConfig.TAG,"onCreateView()")

    }

    override fun onStart() {
        super.onStart()
        CoreLog.v(CoreConfig.TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        CoreLog.v(CoreConfig.TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        CoreLog.v(CoreConfig.TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        CoreLog.v(CoreConfig.TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        CoreLog.v(CoreConfig.TAG, "onDestroy()")
        mBinding?.unbind()
        mBinding = null
        ActivityCollector.remove(weakRefActivity)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        CoreLog.v(CoreConfig.TAG, "onSaveInstanceState()")
    }


    //-------子类需实现抽象方法，或可重写方法-------------------

    /**
     * 在setContentView之前调用此方法
     */
    protected open fun initBeforeSetContentView() {
        initImmersionBar()
    }

    protected abstract fun initViewModel()

    protected abstract fun initGetDataBindingConfig(): DataBindingConfig?

    protected abstract fun initInCreate()

    /**
     * 设置状态切换替代View
     */
    open fun coreGetStatusLayoutReplaceView(): View? {
        return null
    }

    /**
     * Activity的名称，用于友盟页面统计
     *
     * @return
     */
    open fun activityName(): String? {
        return javaClass.simpleName
    }

    /**
     * 沉浸式状态栏设置，子类可重写覆盖
     *
     */
    open fun initImmersionBar() {
        ActivityCollector.setVerticalScreen(this)
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .statusBarDarkFont(true) //状态栏字体是深色，不写默认为亮色
            .flymeOSStatusBarFontColor(R.color.white) //修改flyme OS状态栏字体颜色
            .fitsSystemWindows(true) //使用该属性必须指定状态栏的颜色，不然状态栏透明，很难看
            .init()
    }

    //----------------网络加载-----------------------
    fun finishActivity() {
        KeyboardUtils.hideSoftInput(this)
        finish()
    }

    fun finishAllActivity(){
        KeyboardUtils.hideSoftInput(this)
        finish()
        ActivityCollector.finishAll()
    }

    fun showLoading() {
        CoreLog.v(CoreConfig.TAG, "showLoading()")
        loadingPopup.show()
    }

    fun dismissLoading() {
        CoreLog.v(CoreConfig.TAG, "dismissLoading()")
        loadingPopup.delayDismiss(100)
    }

    //--------基类方法------------------
    /**
     * 基类初始化内容
     */
    private fun initBase() {
        ActivityCollector.add(WeakReference(this))
        weakRefActivity = WeakReference(this)

        initViewModel()
        val dataBindingConfig = initGetDataBindingConfig()
        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        mBinding = DataBindingUtil.setContentView<ViewDataBinding>(
            this,
            dataBindingConfig!!.layout
        )
        mBinding?.lifecycleOwner = this
        mBinding?.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)

        val bindingParams: SparseArray<Any?> = dataBindingConfig.getBindingParams()
        if (bindingParams.size() > 0) {
            for (index in 0 until bindingParams.size()) {
                mBinding?.setVariable(bindingParams.keyAt(index), bindingParams.valueAt(index))
            }
        }
        loadingPopup = XPopup.Builder(this).asLoading(getString(R.string.core_loading))
        setupStatusLayoutManager()
    }

    /**
     * 自定义加载状态配置
     */
    private fun setupStatusLayoutManager() {
        if (null != coreGetStatusLayoutReplaceView() && null == statusLayoutManager) {
            statusLayoutManager = StatusLayoutManager.Builder(coreGetStatusLayoutReplaceView()!!)
                //自定义参数等
                .build()
        }
    }

    //--------基类方法---子类可调用---------------

    /**
     * 加载状态切换
     */
    protected fun changeStatus(status: Int = CoreConfig.StatusLayout.LOADING) {
        if (null != coreGetStatusLayoutReplaceView() && null != statusLayoutManager) {
            when (status) {
                CoreConfig.StatusLayout.LOADING -> {
                    // 加载中
                    statusLayoutManager?.showLoadingLayout()
                }
                CoreConfig.StatusLayout.EMPTY -> {
                    // 空数据
                    statusLayoutManager?.showEmptyLayout()
                }
                CoreConfig.StatusLayout.ERROR -> {
                    // 加载失败
                    statusLayoutManager?.showErrorLayout()
                }
                CoreConfig.StatusLayout.SUCCESS -> {
                    // 加载成功，显示原布局
                    statusLayoutManager?.showSuccessLayout()
                }
            }
        }
    }

    //---------------------
    /**
     * TODO tip: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     * 目前的方案是在 debug 模式下，对获取实例的情况给予提示。
     *
     * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     *
     * @return binding
     */
    protected fun getBinding(): ViewDataBinding? {
        if (AppUtils.isAppDebug() && mBinding != null) {
            CoreLog.w(CoreConfig.TAG, getString(com.hui.core.R.string.core_databinding_warning))
            if(null==mBinding!!.root){
              CoreLog.e(CoreConfig.TAG,"视图为Null")
            }
        }
        return mBinding
    }

    //TODO tip 2: Jetpack 通过 "工厂模式" 来实现 ViewModel 的作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域的 Provider 获得的 ViewModel 实例不是同一个，
    //所以如果 ViewModel 对状态信息的保留不符合预期，可以从这个角度出发去排查 是否眼前的 ViewModel 实例不是目标实例所致。
    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840
    protected open fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider!![modelClass]
    }

    protected open fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(
                (this.applicationContext as BaseApplication)!!,getAppFactory(this)!!
            )
        }
        return mApplicationProvider!![modelClass]
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory? {
        val application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private fun checkApplication(activity: Activity): Application {
        if (null == activity.application) {
            throw IllegalStateException(
                "Your activity/fragment is not yet attached to Application. You can't request ViewModel before onCreate call."
            )
        }
        return activity.application
    }

}