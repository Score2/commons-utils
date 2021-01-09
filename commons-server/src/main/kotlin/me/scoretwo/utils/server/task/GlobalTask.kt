package me.scoretwo.utils.server.task

import me.scoretwo.utils.plugin.GlobalPlugin

interface GlobalTask {
    val id: Int
    val plugin: GlobalPlugin
    val type: TaskType

    fun cancel()
    fun isCancelled(): Boolean
}
// 暂时不作修改
val globalTasks = mutableMapOf<Int, GlobalTask>()