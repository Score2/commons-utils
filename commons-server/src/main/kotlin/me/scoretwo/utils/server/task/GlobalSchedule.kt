package me.scoretwo.utils.server.task

import me.scoretwo.utils.plugin.GlobalPlugin
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Supplier

interface GlobalSchedule {

    fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable): GlobalTask
    fun task(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>) = task(plugin, type, Runnable { supplier.get() })

    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): GlobalTask
    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Int, unit: TimeUnit = TimeUnit.MILLISECONDS) = delay(plugin, type, runnable, time.toLong(), unit)
    fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Double, unit: TimeUnit = TimeUnit.MILLISECONDS) = delay(plugin, type, runnable, time.toLong(), unit)
    fun delay(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) = delay(plugin, type, Runnable { supplier.get() }, time, unit)
    fun delay(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Int, unit: TimeUnit = TimeUnit.MILLISECONDS) = delay(plugin, type, supplier, time.toLong(), unit)
    fun delay(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Double, unit: TimeUnit = TimeUnit.MILLISECONDS) = delay(plugin, type, supplier, time.toLong(), unit)

    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): GlobalTask
    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Int, unit: TimeUnit = TimeUnit.MILLISECONDS) = repeat(plugin, type, runnable, time.toLong(), unit)
    fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Double, unit: TimeUnit = TimeUnit.MILLISECONDS) = repeat(plugin, type, runnable, time.toLong(), unit)
    fun repeat(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) = repeat(plugin, type, Runnable { supplier.get() }, time, unit)
    fun repeat(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Int, unit: TimeUnit = TimeUnit.MILLISECONDS) = repeat(plugin, type, supplier, time.toLong(), unit)
    fun repeat(plugin: GlobalPlugin, type: TaskType, supplier: Supplier<Void>, time: Double, unit: TimeUnit = TimeUnit.MILLISECONDS) = repeat(plugin, type, supplier, time.toLong(), unit)

}

enum class TaskType(val fullName: String) {
    SYNC("sync"),
    ASYNC("Async")
}