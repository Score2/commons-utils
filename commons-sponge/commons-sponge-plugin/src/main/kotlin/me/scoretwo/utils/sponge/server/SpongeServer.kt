package me.scoretwo.utils.sponge.server

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.GlobalCommandMap
import me.scoretwo.utils.event.EventManager
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
import me.scoretwo.utils.sponge.command.*
import me.scoretwo.utils.sponge.plugin.toGlobalPlugin
import me.scoretwo.utils.sponge.plugin.toSpongePlugin
import org.spongepowered.api.Platform
import org.spongepowered.api.Server
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.service.permission.Subject
import java.util.*
import java.util.concurrent.TimeUnit

fun Server.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = ServerBrand.SPONGE.fullName
        override val brand = ServerBrand.SPONGE
        override val version = "${Sponge.getGame().platform.getContainer(Platform.Component.API).version.get()}.x.x"
        override val schedule = Task.builder().toGlobalScheduler()
        override val console: GlobalSender = server.console.toGlobalSender()
        override val commandMap = object : GlobalCommandMap() {
            override fun register(nexus: CommandNexus) {
                super.register(nexus)
                nexus.registerSpongeCommands()
            }

            override fun unregister(nexus: CommandNexus) {
                super.unregister(nexus)
                nexus.unregisterSpongeCommand()
            }
        }
        override val eventManager: EventManager
            get() = TODO("Not yet implemented")
        override fun getPlayer(username: String): Optional<GlobalPlayer> = server.getPlayer(username).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = server.getPlayer(uniqueId).let {
            if (it.isPresent) Optional.ofNullable(it.get().toGlobalPlayer()) else Optional.empty<GlobalPlayer>()
        }

        override fun dispatchCommand(sender: GlobalSender, command: String) = Sponge.getCommandManager().process(sender.toSpongeSender(), command).equals(CommandResult.success())
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.onlinePlayers.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.onlinePlayers.contains(player.toSpongePlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.getPlayer(uniqueId).isPresent
    }.also {
        globalServer = it
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