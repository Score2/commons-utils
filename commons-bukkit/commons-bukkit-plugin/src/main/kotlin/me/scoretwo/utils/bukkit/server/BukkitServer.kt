package me.scoretwo.utils.bukkit.server

import me.scoretwo.utils.bukkit.command.*
import me.scoretwo.utils.bukkit.plugin.toBukkitPlugin
import me.scoretwo.utils.bukkit.plugin.toGlobalPlugin
import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.GlobalCommandMap
import me.scoretwo.utils.event.EventManager
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
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.TimeUnit
import org.apache.commons.lang.RandomStringUtils
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import java.io.File
import java.io.FileOutputStream
import java.util.function.Consumer


fun Server.toGlobalServer(): GlobalServer = this.let { server ->
    if (isGlobalServerAvailable())
        globalServer
    else object : GlobalServer {
        override val name = server.name
        override val brand = ServerBrand.BUKKIT
        override val version = server.version
        override val schedule = server.scheduler.toGlobalScheduler()
        override val console: GlobalSender = server.consoleSender.toGlobalSender()
        override val commandMap = object : GlobalCommandMap() {
            override fun register(nexus: CommandNexus) {
                super.register(nexus)
                nexus.registerBukkitCommands()
            }
            override fun unregister(nexus: CommandNexus) {
                super.unregister(nexus)
                nexus.unregisterBukkitCommand()
            }
        }
        override val eventManager = object : EventManager() {
            override fun generateInstance(plugin: GlobalPlugin, token: String, handlers: MutableMap<String, MutableList<Consumer<Any>>>) {
                val className = "${plugin.name}_${RandomStringUtils.randomAlphabetic(10)}"
                val cw = ClassWriter(0)
                var fv: FieldVisitor
                var mv: MethodVisitor
                var av0: AnnotationVisitor

                cw.visit(
                    52,
                    ACC_PUBLIC + ACC_SUPER,
                    className,
                    null,
                    "java/lang/Object",
                    arrayOf("org/bukkit/event/Listener")
                )

                cw.visitSource("$className.java", null)

                run {
                    fv = cw.visitField(
                        ACC_PUBLIC,
                        "listeners",
                        "Ljava/util/Map;",
                        "Ljava/util/Map<Ljava/lang/String;Ljava/util/function/Consumer<Ljava/lang/Object;>;>;",
                        null
                    )
                    fv.visitEnd()
                }
                run {
                    mv = cw.visitMethod(
                        ACC_PUBLIC,
                        "<init>",
                        "(Ljava/util/Map;)V",
                        "(Ljava/util/Map<Ljava/lang/String;Ljava/util/function/Consumer<Ljava/lang/Object;>;>;)V",
                        null
                    )
                    mv.visitCode()
                    val l0: Label = Label()
                    mv.visitLabel(l0)
                    mv.visitLineNumber(18, l0)
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                    val l1: Label = Label()
                    mv.visitLabel(l1)
                    mv.visitLineNumber(19, l1)
                    mv.visitVarInsn(ALOAD, 0)
                    mv.visitVarInsn(ALOAD, 1)
                    mv.visitFieldInsn(PUTFIELD, className, "listeners", "Ljava/util/Map;")
                    val l2: Label = Label()
                    mv.visitLabel(l2)
                    mv.visitLineNumber(20, l2)
                    mv.visitInsn(RETURN)
                    val l3: Label = Label()
                    mv.visitLabel(l3)
                    mv.visitLocalVariable("this", "L$className;", null, l0, l3, 0)
                    mv.visitLocalVariable(
                        "listeners",
                        "Ljava/util/Map;",
                        "Ljava/util/Map<Ljava/lang/String;Ljava/util/function/Consumer<Ljava/lang/Object;>;>;",
                        l0,
                        l3,
                        1
                    )
                    mv.visitMaxs(2, 2)
                    mv.visitEnd()
                }

                // name, function
                val consumers = mutableMapOf<String, Any>()
                var line = 7
                handlers.forEach { listener: Map.Entry<String, MutableList<Consumer<Any>>> ->
                    line += 3
                    val path = listener.key.replace(".", "/")

                    listener.value.forEach {
                        val methodName = "handle_${RandomStringUtils.randomAlphabetic(10)}"
                        consumers[methodName] = it

                        mv = cw.visitMethod(
                            ACC_PUBLIC,
                            methodName,
                            "(L${path};)V",
                            null,
                            null
                        )
                        run {
                            av0 = mv.visitAnnotation("Lorg/bukkit/event/EventHandler;", true)
                            av0.visitEnd()
                        }
                        mv.visitCode()
                        val l0 = Label()
                        mv.visitLabel(l0)
                        mv.visitLineNumber(line, l0)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitFieldInsn(GETFIELD, className, "listeners", "Ljava/util/Map;")
                        mv.visitLdcInsn(methodName)
                        mv.visitMethodInsn(
                            INVOKEINTERFACE,
                            "java/util/Map",
                            "get",
                            "(Ljava/lang/Object;)Ljava/lang/Object;",
                            true
                        )
                        mv.visitTypeInsn(CHECKCAST, "java/util/function/Consumer")
                        mv.visitVarInsn(ALOAD, 1)
                        mv.visitMethodInsn(
                            INVOKEINTERFACE,
                            "java/util/function/Consumer",
                            "accept",
                            "(Ljava/lang/Object;)V",
                            true
                        )
                        val l1 = Label()
                        mv.visitLabel(l1)
                        mv.visitLineNumber(line + 1, l1)
                        mv.visitInsn(RETURN)
                        val l2 = Label()
                        mv.visitLabel(l2)
                        mv.visitLocalVariable("this", "L${className};", null, l0, l2, 0)
                        mv.visitLocalVariable("e", "L${path};", null, l0, l2, 1)
                        mv.visitMaxs(2, 2)
                        mv.visitEnd()

                    }

                }
                cw.visitEnd()

                val fileOutputStream = FileOutputStream(File("${plugin.dataFolder.name}/cache/listener", "$className.class"))
                fileOutputStream.write(cw.toByteArray())
                fileOutputStream.close()

            }

            override fun registerListener(plugin: GlobalPlugin, token: String, listener: Any?) {
                super.registerListener(plugin, token, listener)
                if (listener !is Listener) {
                    return
                }
                Bukkit.getPluginManager().registerEvents(listener, plugin.toBukkitPlugin())

            }

            override fun unregisterListener(plugin: GlobalPlugin, listener: Any?) {
                super.unregisterListener(plugin, listener)
                if (listener !is Listener) {
                    return
                }
                HandlerList.unregisterAll(listener)
            }
        }
        override fun getPlayer(username: String): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(username)?.toGlobalPlayer())
        override fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer> = Optional.ofNullable(server.getPlayer(uniqueId)?.toGlobalPlayer())
        override fun dispatchCommand(sender: GlobalSender, command: String) = try { server.dispatchCommand(sender.toBukkitSender(), command) } catch (e: Throwable) { false }
        override fun getOnlinePlayers(): Collection<GlobalPlayer> = mutableListOf<GlobalPlayer>().also { globalPlayers -> server.onlinePlayers.forEach { globalPlayers.add(it.toGlobalPlayer()) } }
        override fun isOnlinePlayer(player: GlobalPlayer) = server.onlinePlayers.contains(player.toBukkitPlayer())
        override fun isOnlinePlayer(uniqueId: UUID) = server.onlinePlayers.contains(server.getPlayer(uniqueId))
    }.also {
        globalServer = it
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