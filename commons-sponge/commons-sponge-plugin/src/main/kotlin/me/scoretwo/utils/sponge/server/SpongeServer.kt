package me.scoretwo.utils.sponge.server

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
import me.scoretwo.utils.sponge.command.toGlobalSender
import me.scoretwo.utils.sponge.command.toGlobalPlayer
import me.scoretwo.utils.sponge.command.toSpongePlayer
import me.scoretwo.utils.sponge.plugin.toGlobalPlugin
import me.scoretwo.utils.sponge.plugin.toSpongePlugin
import org.spongepowered.api.Server
import org.spongepowered.api.scheduler.Task
import java.util.*
import java.util.concurrent.TimeUnit

fun Server.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = ServerBrand.SPONGE.fullName
        override val brand = ServerBrand.SPONGE
        override val version = "7.2.0"
        override val schedule = Task.builder().toGlobalScheduler()
        override val console: GlobalSender = server.console.toGlobalSender()
        override fun getPlayer(username: String): Optional<GlobalPlayer> = server.getPlayer(username).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = server.getPlayer(uniqueId).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.onlinePlayers.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.onlinePlayers.contains(player.toSpongePlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.getPlayer(uniqueId).isPresent
    }
}

fun Task.Builder.toGlobalScheduler(): GlobalSchedule = this.let {
    object : GlobalSchedule {
        override fun task(plugin: GlobalPlugin, type: TaskType, runnable: Runnable) = when (type) {
            TaskType.SYNC -> it.execute(runnable)
                .name("${plugin.description.name}-Task-${globalTasks.size + 1}")
                .submit(plugin.toSpongePlugin()).toGlobalTask()
            TaskType.ASYNC -> it.async().execute(runnable).submit(plugin.toSpongePlugin()).toGlobalTask()
        }

        override fun delay(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            TaskType.SYNC -> it.execute(runnable)
                .delay(time, unit)
                .name("${plugin.description.name}-Task-delay-${globalTasks.size + 1}")
                .submit(plugin.toSpongePlugin()).toGlobalTask()
            TaskType.ASYNC -> it.async().execute(runnable)
                .delay(time, unit)
                .name("${plugin.description.name}-Task-delay-${globalTasks.size + 1}")
                .submit(plugin.toSpongePlugin()).toGlobalTask()
        }

        override fun repeat(plugin: GlobalPlugin, type: TaskType, runnable: Runnable, time: Long, unit: TimeUnit) = when (type) {
            TaskType.SYNC -> it.execute(runnable)
                .interval(time, unit)
                .name("${plugin.description.name}-Task-delay-${globalTasks.size + 1}")
                .submit(plugin.toSpongePlugin()).toGlobalTask()
            TaskType.ASYNC -> it.async().execute(runnable)
                .interval(time, unit)
                .name("${plugin.description.name}-Task-delay-${globalTasks.size + 1}")
                .submit(plugin.toSpongePlugin()).toGlobalTask()
        }
    }
}

fun Task.toGlobalTask() = this.let { task ->
    object : GlobalTask {
        private var isCancelled = false
        override val id: Int = (globalTasks.size + 1).also { globalTasks[it] = this }

        override val plugin = task.owner.toGlobalPlugin()
        override val type = if (task.isAsynchronous) TaskType.ASYNC else TaskType.SYNC
        override fun cancel() { task.cancel() }
        override fun isCancelled(): Boolean = isCancelled
    }
}