package me.scoretwo.utils.bukkit.server

import me.scoretwo.utils.bukkit.command.toBukkitPlayer
import me.scoretwo.utils.bukkit.command.toGlobalPlayer
import me.scoretwo.utils.bukkit.command.toGlobalSender
import me.scoretwo.utils.bukkit.plugin.toBukkitPlugin
import me.scoretwo.utils.bukkit.plugin.toGlobalPlugin
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.task.GlobalSchedule
import me.scoretwo.utils.server.GlobalServer
import me.scoretwo.utils.server.ServerBrand
import me.scoretwo.utils.server.globalServer
import me.scoretwo.utils.server.isGlobalServerAvailable
import me.scoretwo.utils.server.task.GlobalTask
import me.scoretwo.utils.server.task.TaskType
import me.scoretwo.utils.server.task.TaskType.*
import me.scoretwo.utils.server.task.globalTasks
import org.bukkit.Server
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.TimeUnit

fun Server.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = server.name
        override val brand = ServerBrand.BUKKIT
        override val version = server.version
        override val schedule = server.scheduler.toGlobalScheduler()
        override val console: GlobalSender = server.consoleSender.toGlobalSender()
        override fun getPlayer(username: String): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(username)?.toGlobalPlayer())
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(uniqueId)?.toGlobalPlayer())
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.onlinePlayers.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.onlinePlayers.contains(player.toBukkitPlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.onlinePlayers.contains(server.getPlayer(uniqueId))
    }
}

fun BukkitScheduler.toGlobalScheduler(): GlobalSchedule = this.let {
    object : GlobalSchedule {
        override fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable) = when (type) {
            SYNC -> it.runTask(plugin.toBukkitPlugin(), runnable).toGlobalTask()
            ASYNC -> it.runTaskAsynchronously(plugin.toBukkitPlugin(), runnable).toGlobalTask()
        }

        override fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            SYNC -> it.runTaskLater(plugin.toBukkitPlugin(), runnable, unit.convert(time, TimeUnit.MILLISECONDS) / 20).toGlobalTask()
            ASYNC -> it.runTaskLaterAsynchronously(plugin.toBukkitPlugin(), runnable, unit.convert(time, TimeUnit.MILLISECONDS) / 20).toGlobalTask()
        }

        override fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            SYNC -> it.runTaskTimer(plugin.toBukkitPlugin(), runnable, 0L, unit.convert(time, TimeUnit.MILLISECONDS) / 20).toGlobalTask()
            ASYNC -> it.runTaskTimerAsynchronously(plugin.toBukkitPlugin(), runnable, 0L, unit.convert(time, TimeUnit.MILLISECONDS) / 20).toGlobalTask()
        }
    }
}

fun BukkitTask.toGlobalTask() = this.let { task ->
    object : GlobalTask {
        override val id: Int = (globalTasks.size + 1).also { globalTasks[it] = this }
        override val plugin = task.owner.toGlobalPlugin()
        override val type = if (task.isSync) SYNC else ASYNC
        override fun cancel() = task.cancel()
        override fun isCancelled() = task.isCancelled
    }
}