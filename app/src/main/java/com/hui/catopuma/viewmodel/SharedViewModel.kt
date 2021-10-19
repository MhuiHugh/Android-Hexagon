package com.hui.catopuma.viewmodel

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData


/**
 *    author : Mhui
 *    e-mail :  hughm@foxmail.com
 *    date   : 2021/2/317:23
 *    desc   :
 * TODO tip 1：callback-ViewModel 的职责仅限于在 "跨页面通信" 的场景下，承担 "唯一可信源"，
 * 所有跨页面的 "状态同步请求" 都交由该可信源在内部决策和处理，并统一分发给所有订阅者页面。
 * <p>
 * 如果这样说还不理解的话，详见《LiveData 鲜为人知的 身世背景 和 独特使命》中结合实际场合 对"唯一可信源"本质的解析。
 * https://xiaozhuanlan.com/topic/0168753249
 * <p>
 * Create by KunMinX at 19/10/16
 **/

public class SharedViewModel : BaseViewModel() {

    //TODO tip 2：此处演示通过 UnPeekLiveData 配合 SharedViewModel 来发送 生命周期安全的、
    // 确保消息同步一致性和可靠性的 "跨页面" 通知。
    //TODO tip 3：并且，在 "页面通信" 的场景下，使用全局 ViewModel，是因为它被封装在 base 页面中，
    // 避免页面之外的组件拿到，从而造成不可预期的推送。
    // 而且尽可能使用单例或 ViewModel 托管 liveData，这样能利用好 LiveData "读写分离" 的特性
    // 来实现 "唯一可信源" 单向数据流的决策和分发，从而避免只读数据被篡改 导致的其他页面拿到脏数据。
    // 如果这么说还不理解的话，
    // 详见 https://xiaozhuanlan.com/topic/0168753249 和 https://xiaozhuanlan.com/topic/6257931840
    private val exitApp: UnPeekLiveData<Boolean> = UnPeekLiveData<Boolean>()

    //TODO tip 4：可以通过构造器的方式来配置 UnPeekLiveData
    /**
     * 具体存在有缘和使用方式可详见《LiveData 数据倒灌 背景缘由全貌 独家解析》
     * https://xiaozhuanlan.com/topic/6719328450
     */
    public fun setExitApp(exit: Boolean) {
        exitApp.value = exit
    }

    public fun isExitApp(): ProtectedUnPeekLiveData<Boolean?>? {
        return exitApp
    }

}