package com.hui.catopuma.page.web

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.chw.permissionx.PermissionXUtils
import com.hjq.bar.OnTitleBarListener
import com.hjq.toast.ToastUtils
import com.hui.catopuma.BR
import com.hui.catopuma.R
import com.hui.catopuma.data.Constant
import com.hui.core.base.BaseActivity
import com.hui.core.base.DataBindingConfig
import com.hui.core.utils.CoreConfig
import com.hui.core.utils.CoreLog
import com.hui.core.utils.LogUtils
import com.hui.core.widget.dsbridge.DWebView
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsReaderView
import com.tencent.smtt.sdk.ValueCallback
import org.json.JSONException
import org.json.JSONObject


/**
 * 通用h5加载
 */
class WebActivity : BaseActivity() {

    lateinit var mViewModel: WebViewModel
    lateinit var click: WebActivity.ClickProxy

    var mWeb: DWebView? = null
    var rootLL:LinearLayout?=null

    /**
     * 文件显示
     */
    lateinit var tbsReaderView: TbsReaderView

    //-----------------------------------

    /**
     * 根据action处理交互,100通用网页加载
     */
    var mAction=100

    //-----------------------------------

    companion object {
        val WEB_PAGE=100
        val WEB_FILE=101

        fun startWebActivity(mContext:Context?, loadUri:String?, title:String?="", rightActionString:String?="",
                             rightActionDrawableId:Int?=0, action:Int?= WEB_PAGE, showTitle:Boolean=true, showTitleLine:Boolean=true, showLoadProgress:Boolean=true){
            if(null!=mContext){
                val intent=Intent(mContext,WebActivity::class.java)
                intent.putExtra("loadUri",loadUri)
                intent.putExtra("title",title)
                intent.putExtra("rightActionString",rightActionString)
                intent.putExtra("rightActionDrawableId",rightActionDrawableId)
                intent.putExtra("showTitleBar",showTitle)
                intent.putExtra("showTitleBarLine",showTitleLine)
                intent.putExtra("showLoadProgress",showLoadProgress)
                intent.putExtra("action",action)
                mContext.startActivity(intent)
            }
        }
    }

    //-------------------------------------

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(WebViewModel::class.java)
    }

    override fun initGetDataBindingConfig(): DataBindingConfig? {
        click = ClickProxy()
        return DataBindingConfig(R.layout.activity_web, BR.vm, mViewModel)
            .addBindingParam(BR.click, click)
    }

    override fun initInCreate() {
        //视图初始化
        rootLL=getBinding()?.root?.findViewById(R.id.web_ll)
        mWeb=getBinding()?.root?.findViewById(R.id.web_dwebview)

        mAction=intent.getIntExtra("action",WEB_PAGE)

        var mUri=intent.getStringExtra("loadUri")
        if(mUri.isNullOrEmpty()){
            //数据异常
        }else if(mUri.startsWith("www")){
            mUri="http://$mUri"
        }
        if(WEB_PAGE==mAction){
             showWeb(mUri)
        }else if(WEB_FILE==mAction){
            PermissionXUtils.requestPermissions(
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,showLog = true,
                onPermissionGranded = {
                    // 权限全部申请通过
                    loadFile(mUri)
                },
                onPermissionDined = { dinedList, noShowRationableList ->
                    // dinedList：被拒绝的权限集合。noShowRationableList：被拒绝且不再显示弹窗的集合
                    dinedList.forEach {
                        PermissionXUtils.logD(" 权限被拒绝 = $it ")
                    }
                    noShowRationableList.forEach {
                        PermissionXUtils.logD(" 权限被拒绝且不显示弹窗 = $it ")
                    }
                })
        }

        mViewModel.title.set(intent.getStringExtra("title"))
        mViewModel.rightActionString.set(intent.getStringExtra("rightActionString"))
        mViewModel.rightActionDrawableId.set(intent.getIntExtra("rightActionDrawableId",0))
        mViewModel.showTitleBar.set(intent.getBooleanExtra("showTitleBar",true))
        mViewModel.showTitleBarLine.set(intent.getBooleanExtra("showTitleBarLine",true))
        mViewModel.showLoadProgress.set(intent.getBooleanExtra("showLoadProgress",true))

    }

    override fun onDestroy() {
        super.onDestroy()
        QbSdk.closeFileReader(this)
    }

    //-------------------------------------

    private fun showWeb(mUri:String?){
        mWeb?.visibility=View.VISIBLE
        mViewModel.webUri.set(mUri)
    }

   private fun loadFile(mUri:String?){
       if(mUri.isNullOrEmpty()){
           ToastUtils.show(getString(R.string.common_uri_error))
       }else{
           showLoading()
           mViewModel.downLoadFile(mUri)
           mViewModel.downloadFilePath.observeInActivity(this) {
              LogUtils.v("文件下载结束 $it")
              dismissLoading()
              showFile(it)
           }
       }
    }

    private fun showFile(mUri: String?){
        if(mUri.isNullOrEmpty()){
            ToastUtils.show(getString(R.string.common_uri_error))
        }else if(FileUtils.isFile(mUri)){
            LogUtils.v("x5内核加载文件... $mUri")
//            QbSdk.forceSysWebView()
//
//            val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
//                override fun onViewInitFinished(arg0: Boolean) {
//                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                    CoreLog.d( CoreConfig.TAG,"x5内核初始化 onViewInitFinished is $arg0")
//                }
//
//                override fun onCoreInitFinished() {
//                    CoreLog.d( CoreConfig.TAG,"onCoreInitFinished()")
//                }
//            }
//            QbSdk.initX5Environment(this, cb)

          val params:HashMap<String,String> = HashMap()
             params["style"] = "1"
             params["local"] = "true"
            params["entryId"] = "2"
            params["allowAutoDestory"] = "true"
            val obj = JSONObject()
            try {
                obj.put("pkgName", AppUtils.getAppPackageName())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            params["menuData"] = obj.toString()

             val callback= ValueCallback<String> {
                 LogUtils.d("文件加载:$it")
                 if("-1"!=it){
                     finishActivity()
                 }
             }
            QbSdk.openFileReader(this,mUri,params,callback)
        }else{
            ToastUtils.show(getString(R.string.common_no_file))
        }
    }

    //-------------------------------------

    override fun onBackPressed() {
        LogUtils.d("onBackPressed()")
        if (null != mWeb && mWeb!!.canGoBack()) {
            mWeb!!.goBack()
        } else {
            click.getClick().onLeftClick(null)
        }
    }

    //-------------------------------------

    /**
     * 点击事件
     */
    inner class ClickProxy: OnTitleBarListener,DWebView.OnDWebViewListener, JsCallNativeApi,NativeCallJsApi {

        override fun onLeftClick(view: View?) {
            LogUtils.d("onLeftClick()")
            finishActivity()
        }

        override fun onTitleClick(view: View?) {

        }

        override fun onRightClick(view: View?) {

        }

        //----------------

        fun clickTest() {

        }

        //---------JsApi ----------
        override fun getActivity(): WebActivity {
            return this@WebActivity
        }

        override fun getClick(): ClickProxy {
          return this
        }

        //---------NativeCallJs---------------

        override fun callJsBack(method: String, success: Boolean) {
           if(""==method){

           }
        }

        //--------DWebView.OnDWebViewListener --------
        override fun onPageStarted(webView: DWebView?, url: String?, bitmap: Bitmap?) {
            LogUtils.d("${Constant.TAG} onPageStarted() $url")
           if(mViewModel.showLoadProgress.get()){
               mViewModel.showLoadProgress.set(true)
           }
        }

        override fun onReceivedTitle(view: DWebView?, title: String?) {
            if(mViewModel.title.get().isNullOrEmpty()){
                mViewModel.title.set(title)
            }
        }

        override fun onProgressChanged(view: DWebView?, newProgress: Int) {
            if(mViewModel.showLoadProgress.get()){
                mViewModel.loadProgress.set(newProgress)
            }
        }

        override fun onPageFinished(webView: DWebView?, url: String?) {
            if(mViewModel.showLoadProgress.get()){
                mViewModel.showLoadProgress.set(false)
            }
        }
    }
}