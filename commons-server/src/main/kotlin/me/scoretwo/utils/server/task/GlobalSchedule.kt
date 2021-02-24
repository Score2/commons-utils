package me.scoretwo.utils.server.task

import me.scoretwo.utils.plugin.GlobalPlugin
import java.util.concurrent.TimeUnit

interface GlobalSchedule {

    fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable): GlobalTask

    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit): GlobalTask
    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Int, unit: TimeUnit) = delay(plugin, type, runnable, time.toLong(), unit)
    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Double, unit: TimeUnit) = delay(plugin, type, runnable, time.toLong(), unit)

    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit): GlobalTask
    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Int, unit: TimeUnit) = repeat(plugin, type, runnable, time.toLong(), unit)
    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Double, unit: TimeUnit) = repeat(plugin, type, runnable, time.toLong(), unit)

}

enum class TaskType(val fullName: String) {
    SYNC("sync"),
    ASYNC("Async")
}