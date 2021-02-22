package me.scoretwo.utils.bungee.server

import me.scoretwo.utils.bungee.command.toBungeePlayer
import me.scoretwo.utils.bungee.command.toBungeeSender
import me.scoretwo.utils.bungee.command.toGlobalPlayer
import me.scoretwo.utils.bungee.command.toGlobalSender
import me.scoretwo.utils.bungee.plugin.toBungeePlugin
import me.scoretwo.utils.bungee.plugin.toGlobalPlugin
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.GlobalServer
import me.scoretwo.utils.server.ServerBrand
import me.scoretwo.utils.server.globalServer
import me.scoretwo.utils.server.isGlobalServerAvailable
import me.scoretwo.utils.server.task.GlobalSchedule
import me.scoretwo.utils.server.task.GlobalTask
import me.scoretwo.utils.server.task.TaskType
import me.scoretwo.utils.server.task.globalTasks
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.scheduler.ScheduledTask
import net.md_5.bungee.api.scheduler.TaskScheduler
import java.util.*
import java.util.concurrent.TimeUnit

fun ProxyServer.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = server.name
        override val brand = ServerBrand.BUNGEECORD
        override val version = server.version
        override val schedule = server.scheduler.toGlobalScheduler()
        override val console = server.console.toGlobalSender()
        override fun getPlayer(username: String): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(username)?.toGlobalPlayer())
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(uniqueId)?.toGlobalPlayer())
        override fun dispatchCommand(sender: GlobalSender, command: String) = try { server.pluginManager.dispatchCommand(sender.toBungeeSender(), command) } catch (e: Throwable) { false }
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.players.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.players.contains(player.toBungeePlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.players.contains(server.getPlayer(uniqueId))
    }.also {
        globalServer = it
    }
}

fun TaskScheduler.toGlobalScheduler() = this.let {
    object : GlobalSchedule {
        override fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable) =
            it.runAsync(plugin.toBungeePlugin(), runnable).toGlobalTask()
        override fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) =
            it.schedule(plugin.toBungeePlugin(), runnable, time, unit).toGlobalTask()
        override fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) =
            it.schedule(plugin.toBungeePlugin(), runnable, 0L, time, unit).toGlobalTask()
    }
}

fun ScheduledTask.toGlobalTask() = this.let { task ->
    object : GlobalTask {
        private var isCancelled = false
        override val id: Int = (globalTasks.size + 1).also { globalTasks[it] = this }

        override val plugin = task.owner.toGlobalPlugin()
        override val type = TaskType.ASYNC
        override fun cancel() = task.cancel().also {
            isCancelled = true
        }
        override fun isCancelled(): Boolean = isCancelled
    }
}