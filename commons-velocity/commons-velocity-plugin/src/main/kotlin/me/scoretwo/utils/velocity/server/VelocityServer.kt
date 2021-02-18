package me.scoretwo.utils.velocity.server

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import com.velocitypowered.api.scheduler.Scheduler
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
import me.scoretwo.utils.velocity.command.toGlobalPlayer
import me.scoretwo.utils.velocity.command.toGlobalSender
import me.scoretwo.utils.velocity.command.toVelocityPlayer
import me.scoretwo.utils.velocity.plugin.toVelocityPlugin
import java.util.*
import java.util.concurrent.TimeUnit

lateinit var proxyServer: ProxyServer

fun ProxyServer.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = ServerBrand.VELOCITY.fullName
        override val brand = ServerBrand.VELOCITY
        override val version = server.version.version
        override val schedule = server.scheduler.toGlobalScheduler()
        override val console: GlobalSender = server.consoleCommandSource.toGlobalSender()
        override fun getPlayer(username: String): Optional<GlobalPlayer> = server.getPlayer(username).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = server.getPlayer(uniqueId).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.allPlayers.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.allPlayers.contains(player.toVelocityPlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.getPlayer(uniqueId).isPresent
    }.also {
        globalServer = it
    }
}

fun Scheduler.toGlobalScheduler(): GlobalSchedule = this.let {
    object : GlobalSchedule {
        override fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable) =
            it.buildTask(plugin.toVelocityPlugin(), runnable).schedule().toGlobalTask(plugin)

        override fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) =
            it.buildTask(plugin.toVelocityPlugin(), runnable).delay(time, unit).schedule().toGlobalTask(plugin)

        override fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) =
            it.buildTask(plugin.toVelocityPlugin(), runnable).repeat(time, unit).schedule().toGlobalTask(plugin)
    }
}

fun ScheduledTask.toGlobalTask(plugin: GlobalPlugin) = this.let { task ->
    object : GlobalTask {
        private var isCancelled = false
        override val id: Int = (globalTasks.size + 1).also { globalTasks[it] = this }

        override val plugin = plugin
        override val type = TaskType.ASYNC
        override fun cancel() { task.cancel() }
        override fun isCancelled(): Boolean = isCancelled
    }
}