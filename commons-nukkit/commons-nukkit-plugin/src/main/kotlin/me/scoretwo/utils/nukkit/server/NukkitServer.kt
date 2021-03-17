package me.scoretwo.utils.nukkit.server

import cn.nukkit.Nukkit
import cn.nukkit.Server
import cn.nukkit.scheduler.AsyncTask
import cn.nukkit.scheduler.ServerScheduler
import cn.nukkit.scheduler.Task
import cn.nukkit.scheduler.TaskHandler
import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.GlobalCommandMap
import me.scoretwo.utils.event.EventManager
import me.scoretwo.utils.nukkit.command.*
import me.scoretwo.utils.nukkit.plugin.toGlobalPlugin
import me.scoretwo.utils.nukkit.plugin.toNukkitPlugin
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.*
import me.scoretwo.utils.server.task.GlobalSchedule
import me.scoretwo.utils.server.task.GlobalTask
import me.scoretwo.utils.server.task.TaskType
import me.scoretwo.utils.server.task.globalTasks
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author 83669
 * @date 2021/3/17 20:28
 *
 * @project commons-utils
 */

fun Server.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = server.name
        override val brand = ServerBrand.NUKKIT
        override val version = server.version
        override val schedule = server.scheduler.toGlobalScheduler()
        override val console: GlobalSender = server.consoleSender.toGlobalSender()
        override val commandMap = object : GlobalCommandMap() {
            override fun register(nexus: CommandNexus) {
                super.register(nexus)
                nexus.registerNukkitCommands()
            }
            override fun unregister(nexus: CommandNexus) {
                super.unregister(nexus)
                nexus.unregisterNukkitCommand()
            }
        }
        override val eventManager: EventManager
            get() = TODO("Not yet implemented")
        override fun getPlayer(username: String) = Optional.ofNullable(server.getPlayer(username).toGlobalPlayer())
        override fun getPlayer(uniqueId: UUID) = Optional.ofNullable(server.getPlayer(uniqueId).getOrNull()?.toGlobalPlayer())
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { list ->
            server.onlinePlayers.values.mapTo(list) { it.toGlobalPlayer() }
        }
        override fun isOnlinePlayer(player: GlobalPlayer): Boolean {
            server.onlinePlayers.forEach { if (it.value == player.toNukkitPlayer()) return true }
            return false
        }
        override fun isOnlinePlayer(uniqueId: UUID): Boolean {
            server.onlinePlayers.forEach { if (it.key == uniqueId) return true }
            return false
        }
        override fun dispatchCommand(sender: GlobalSender, command: String) = server.dispatchCommand(sender.toNukkitSender(), command)

    }
}

fun Runnable.toAsyncTask(): AsyncTask = this.let { object : AsyncTask() { override fun onRun() { it.run() } } }
fun Runnable.toCustomTask(plugin: GlobalPlugin): CustomTask = this.let { object : CustomTask(plugin) { override fun onRun(currentTick: Int) { it.run() } } }

fun ServerScheduler.toGlobalScheduler() = this.let {
    object : GlobalSchedule {
        override fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable) = when (type) {
            TaskType.SYNC -> it.scheduleTask(plugin.toNukkitPlugin(), runnable)
            TaskType.ASYNC -> it.scheduleAsyncTask(plugin.toNukkitPlugin(), runnable.toAsyncTask())
        }.toGlobalTask()

        override fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            TaskType.SYNC -> it.scheduleDelayedTask(runnable.toCustomTask(plugin), (unit.convert(time, TimeUnit.MILLISECONDS) / 20).toInt())
            TaskType.ASYNC -> it.scheduleDelayedTask(runnable.toCustomTask(plugin), (unit.convert(time, TimeUnit.MILLISECONDS) / 20).toInt(), true)
        }.toGlobalTask()

        override fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            TaskType.SYNC -> it.scheduleRepeatingTask(runnable.toCustomTask(plugin), (unit.convert(time, TimeUnit.MILLISECONDS) / 20).toInt())
            TaskType.ASYNC -> it.scheduleRepeatingTask(runnable.toCustomTask(plugin), (unit.convert(time, TimeUnit.MILLISECONDS) / 20).toInt(), true)
        }.toGlobalTask()

    }
}

fun TaskHandler.toGlobalTask() = this.let { task ->
    object : GlobalTask {
        override val id: Int = (globalTasks.size + 1).also { globalTasks[it] = this }
        override val plugin = task.plugin.toGlobalPlugin()
        override val type = if (task.isAsynchronous) TaskType.ASYNC else TaskType.SYNC
        override fun cancel() = task.cancel()
        override fun isCancelled() = task.isCancelled
    }
}

abstract class CustomTask(val plugin: GlobalPlugin): Task() {

}