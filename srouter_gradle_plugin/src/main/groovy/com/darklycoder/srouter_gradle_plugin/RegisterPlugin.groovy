package com.darklycoder.srouter_gradle_plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自动注册插件入口
 * @author billy.qi
 * @since 17/3/14 17:35
 */
class RegisterPlugin implements Plugin<Project> {

    public static final String EXT_NAME = 'autoregister'

    @Override
    void apply(Project project) {

        /**
         * 注册transform接口
         */
        def isApp = project.plugins.hasPlugin(AppPlugin)
        project.extensions.create(EXT_NAME, AutoRegisterConfig)
        if (isApp) {
            println 'project(' + project.name + ') apply auto-register plugin'
            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new RegisterTransform(project)
            android.registerTransform(transformImpl)
            project.afterEvaluate {
                init(project, transformImpl)//此处要先于transformImpl.transform方法执行
            }
        }
    }

    static void init(Project project, RegisterTransform transformImpl) {
        AutoRegisterConfig config = project.extensions.findByName(EXT_NAME) as AutoRegisterConfig
        config.project = project

        //加入内置的
        def registerInfo = new RegisterInfo()
        registerInfo.interfaceName = "com.darklycoder.srouter.info.IRouterTable"
        registerInfo.initClassName = "com.darklycoder.srouter.RouterTable"
        registerInfo.registerMethodName = "insertRouterTable"
        registerInfo.init()
        if (registerInfo.validate()) {
            config.addRegisterInfo(registerInfo)
        }

        config.convertConfig()

        transformImpl.config = config
    }

}
