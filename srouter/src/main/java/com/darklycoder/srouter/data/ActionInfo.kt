package com.darklycoder.srouter.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.darklycoder.srouter.annotation.info.RouterMetaInfo
import com.darklycoder.srouter.listener.ActionCallback

/**
 * 操作
 * @author DarklyCoder 2018/10/24
 */
class ActionInfo private constructor() {

    var type: ActionType? = null
    var context: Context? = null
    var path: String? = null
    var requestCode: Int? = null
    var argument: Bundle? = null
    var callback: ActionCallback? = null

    constructor(builder: Builder?) : this() {
        this.type = builder?.type
        this.context = builder?.context
        this.path = builder?.path
        this.requestCode = builder?.requestCode
        this.argument = builder?.argument
        this.callback = builder?.callback
    }

    /**
     * 是否有效
     */
    fun isValid(): Boolean {
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
            intent.putExtra("args", argument)
            return intent

        } catch (e: Exception) {

        }

        return null
    }

    class Builder {

        var type: ActionType? = null
        var context: Context? = null
        var path: String? = null
        var requestCode: Int? = null
        var argument: Bundle? = null
        var callback: ActionCallback? = null

        fun newActivity(context: Context, path: String): ActionInfo {
            return newType(ActionType.ACTION_ACTIVITY).newContext(context).newPath(path).build()
        }

        fun newActivityForResult(context: Context, path: String, requestCode: Int): ActionInfo {
            return newType(ActionType.ACTION_ACTIVITY_FOR_RESULT).newRequestCode(requestCode).newActivity(context, path)
        }

        fun newType(type: ActionType): Builder {
            this.type = type
            return this
        }

        fun newContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun newPath(path: String): Builder {
            this.path = path
            return this
        }

        fun newRequestCode(requestCode: Int): Builder {
            this.requestCode = requestCode
            return this
        }

        fun newBundle(argument: Bundle): Builder {
            this.argument = argument
            return this
        }

        fun newCallback(callback: ActionCallback): Builder {
            this.callback = callback
            return this
        }

        fun build(): ActionInfo {
            return ActionInfo(this)
        }

    }

}