package com.darklycoder.srouter

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import android.util.Log
import com.darklycoder.srouter.data.ActionInfo
import com.darklycoder.srouter.data.ActionType
import com.darklycoder.srouter.info.RouterMetaInfo
import org.json.JSONArray
import java.io.InputStream
import java.lang.ref.WeakReference

/**
 * 路由
 * @author DarklyCoder 2018/10/24
 */
class SRouter private constructor() {

    private var mContext: WeakReference<Application>? = null
    private var map = hashMapOf<String, RouterMetaInfo>()
    private var isInit = false
    private lateinit var config: SRouterConfig
    //记录操作
    private val actionList = arrayListOf<ActionInfo>()

    private object Holder {
        val INSTANCE = SRouter()
    }

    companion object {
        const val TAG = "SRouter"
        const val ASSET_NAME = "srouter"
        val instance by lazy { Holder.INSTANCE }
    }

    fun getSRouterConfig(): SRouterConfig {
        return config
    }

    /**
     * 初始化路由
     * @param map 自定义路由
     * @param config 路由基础设置
     */
    fun init(context: Application, map: HashMap<String, RouterMetaInfo>? = null, config: SRouterConfig) {
        if (this.isInit) {
            return
        }

        this.mContext = WeakReference(context)
        this.config = config

        this.map.clear()
        if (null != map) {
            this.map.putAll(map)

            if (config.isOpenLog) {
                Log.e(TAG, "自定义路由映射：${this.map.size}")
            }
        }

        if (config.isUseJson) {
            parseRouterMapFromAssets(context)

        } else {
            //添加自动路由映射
            this.map.putAll(RouterTable.getInstance().routerTable)
            Log.e(TAG, "自动路由映射：${this.map.size}")
        }

        this.isInit = true
    }

    /**
     * 从assets目录下解析路由表
     */
    private fun parseRouterMapFromAssets(context: Application) {
        Thread(Runnable {

            val list = context.assets.list(ASSET_NAME)
            val tmpMap = hashMapOf<String, RouterMetaInfo>()

            list?.forEach {

                var ins: InputStream? = null
                try {
                    ins = context.assets.open("$ASSET_NAME/$it")

                    val size = ins.available()
                    val buffer = ByteArray(size)
                    ins.read(buffer)

                    val content = String(buffer)

                    if (config.isOpenLog) {
                        Log.e(TAG, "路由表$it=>$content")
                    }

                    val jsonArr = JSONArray(content)
                    val arrSize = jsonArr.length()
                    for (i in 0 until arrSize) {
                        val jsonObj = jsonArr.optJSONObject(i)

                        val metaInfo = RouterMetaInfo(
                            jsonObj.optString("route"),
                            jsonObj.optString("path"),
                            jsonObj.optInt("extra")
                        )

                        tmpMap[metaInfo.route] = metaInfo
                    }

                } catch (e: Exception) {
                    Log.e(TAG, e.message)

                } catch (e: OutOfMemoryError) {
                    Log.e(TAG, "内存溢出啦")

                } finally {
                    try {
                        ins?.close()

                    } catch (e: Exception) {
                        Log.e(TAG, e.message)
                    }
                }
            }

            this.map.putAll(tmpMap)

            if (config.isOpenLog) {
                Log.e(TAG, "解析asset路由映射：${map.size}")
            }

        }).start()
    }

    /**
     * 执行操作
     */
    fun run(action: ActionInfo?) {
        if (null == action) {
            return
        }

        if (!action.isValid()) {
            action.callback?.onFail("参数无效")
            return
        }

        val metaInfo = map[action.path]

        if (null == metaInfo) {
            action.callback?.onFail("组件未注册")
            return
        }

        //TODO metaInfo是否有拦截器(全局和局部拦截)

        val intent = action.createIntent(metaInfo)
        if (null == intent) {
            action.callback?.onFail("无法创建Intent")
            return
        }

        //记录操作
        actionList.add(action)

        try {
            when (action.type) {
                ActionType.ACTION_ACTIVITY -> {
                    action.context?.startActivity(intent)
                }

                ActionType.ACTION_ACTIVITY_FOR_RESULT -> {
                    val context = action.context
                    when (context) {
                        is Activity -> context.startActivityForResult(intent, action.requestCode ?: 0)
                        is Fragment -> context.startActivityForResult(intent, action.requestCode ?: 0)
                        else -> action.context?.startActivity(intent)
                    }
                }
            }

            action.callback?.onResult()

        } catch (e: Exception) {
            action.callback?.onFail("组件未注册", e)
        }

        //执行完毕，移除操作
        actionList.remove(action)
    }

}