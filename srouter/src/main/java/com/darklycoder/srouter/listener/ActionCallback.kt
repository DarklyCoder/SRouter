package com.darklycoder.srouter.listener

/**
 * 回调
 * @author DarklyCoder 2018/10/24
 */
interface ActionCallback {

    fun onResult()

    fun onFail(err: String? = null, e: Throwable? = null)

}