/*
 *
 *  * Copyright 2018-present KunMinX
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.hui.core.base

import android.util.SparseArray
import androidx.lifecycle.ViewModel

/**
 * 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
 * 通过这样的方式，来彻底解决 视图调用的一致性问题，
 * 如此，视图实例的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
 * 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。
 *
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
 *
 */
class DataBindingConfig(val layout: Int, val vmVariableId: Int, val stateViewModel: ViewModel) {

    private val bindingParams: SparseArray<Any?> = SparseArray<Any?>()

    fun addBindingParam(variableId: Int, data: Any): DataBindingConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, data)
        }
        return this
    }

    public fun getBindingParams(): SparseArray<Any?> {
        return bindingParams
    }

}