package com.darklycoder.srouter.demo

import android.app.Application
import com.darklycoder.srouter.SRouter
import com.darklycoder.srouter.SRouterConfig
import com.darklycoder.srouter.annotation.Pack

/**
 * @author DarklyCoder
 * @Description
 * @date 2018/10/25
 */
@Pack("com.darklycoder.srouter.demo", table = "MainRouterTable")
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化路由
        val routerConfig = SRouterConfig()
            .setOpenLog(BuildConfig.DEBUG)

        SRouter.instance.init(this, null, routerConfig)
    }

}