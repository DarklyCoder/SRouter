package com.darklycoder.srouter

/**
 * 全局配置
 * @author DarklyCoder 2018/10/24
 */
class SRouterConfig {

    var isOpenLog: Boolean = true//是否开启日志
    var isUseJson: Boolean = false//是否使用Json路由配置
    var isUseCustom: Boolean = false//是否使用自定义路由配置

    fun setOpenLog(flag: Boolean): SRouterConfig {
        this.isOpenLog = flag
        return this
    }

    fun setUseJson(flag: Boolean): SRouterConfig {
        this.isUseJson = flag
        return this
    }

    fun setUseCustom(flag: Boolean): SRouterConfig {
        this.isUseCustom = flag
        return this
    }

}