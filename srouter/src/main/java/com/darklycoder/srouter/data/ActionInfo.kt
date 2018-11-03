package com.darklycoder.srouter.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.darklycoder.srouter.SRouter
import com.darklycoder.srouter.info.RouterMetaInfo
import com.darklycoder.srouter.listener.ActionCallback

/**
 * 操作
 * @author DarklyCoder 2018/10/24
 */
class ActionInfo {

    var type: ActionType? = null
    var context: Context? = null
    var path: String? = null
    var requestCode: Int? = null
    var argument: Bundle? = null
    var isNewTask: Boolean = false
    var isClearTask: Boolean = false
    var callback: ActionCallback? = null

    companion object {
        const val ARGS = "args"
    }

    /**
     * 是否有效
     */
    fun isValid(): Boolean {
        //判断path是否有效
        return null != type
                && null != context
                && null != path
    }

    /**
     * 创建Intent
     */
    fun createIntent(metaInfo: RouterMetaInfo): Intent? {
        try {
            val intent = Intent(context, Class.forName(metaInfo.path))
            intent.putExtra(ARGS, argument)

            if (isNewTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (isClearTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }

            return intent

        } catch (e: Exception) {
            if (SRouter.instance.getSRouterConfig().isOpenLog) {
                Log.e(SRouter.TAG, e.message)
            }
        }

        return null
    }

    fun newActivity(context: Context, path: String): ActionInfo {
        return newType(ActionType.ACTION_ACTIVITY).newContext(context).newPath(path)
    }

    fun newActivityForResult(context: Context, path: String, requestCode: Int): ActionInfo {
        return newType(ActionType.ACTION_ACTIVITY_FOR_RESULT).newRequestCode(requestCode).newActivity(context, path)
    }

    fun newType(type: ActionType): ActionInfo {
        this.type = type
        return this
    }

    fun newContext(context: Context): ActionInfo {
        this.context = context
        return this
    }

    fun newPath(path: String): ActionInfo {
        this.path = path
        return this
    }

    fun newRequestCode(requestCode: Int): ActionInfo {
        this.requestCode = requestCode
        return this
    }

    fun newBundle(argument: Bundle): ActionInfo {
        this.argument = argument
        return this
    }

    fun isNewTask(isNewTask: Boolean): ActionInfo {
        this.isNewTask = isNewTask
        return this
    }

    fun isClearTask(isClearTask: Boolean): ActionInfo {
        this.isClearTask = isClearTask
        return this
    }

    fun newCallback(callback: ActionCallback): ActionInfo {
        this.callback = callback
        return this
    }

}