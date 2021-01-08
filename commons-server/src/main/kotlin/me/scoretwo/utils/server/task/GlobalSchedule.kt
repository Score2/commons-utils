package me.scoretwo.utils.server.task

import me.scoretwo.utils.plugin.GlobalPlugin
import java.util.concurrent.TimeUnit

interface GlobalSchedule {

    fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable): GlobalTask
    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit): GlobalTask
    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit): GlobalTask

}

enum class TaskType(val fullName: String) {
    SYNC("sync"),
    ASYNC("Async")
}