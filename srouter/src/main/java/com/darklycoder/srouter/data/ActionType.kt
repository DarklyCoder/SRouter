package com.darklycoder.srouter.data

/**
 * 操作类型
 * @author DarklyCoder 2018/10/24
 */
enum class ActionType(var type: Int, var desc: String) {

    ACTION_ACTIVITY(1, "打开activity"),
    ACTION_ACTIVITY_FOR_RESULT(2, "打开activity for result");

    fun findAction(type: Int?): ActionType? {
        return when (type) {
            ACTION_ACTIVITY.type -> ACTION_ACTIVITY
            ACTION_ACTIVITY_FOR_RESULT.type -> ACTION_ACTIVITY_FOR_RESULT
            else -> null
        }
    }

}