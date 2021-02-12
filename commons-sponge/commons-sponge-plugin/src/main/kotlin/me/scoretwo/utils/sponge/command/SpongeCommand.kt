package me.scoretwo.utils.sponge.command

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.task.TaskType
import me.scoretwo.utils.sponge.plugin.toSpongePlugin
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.text.TextTemplate
import java.util.*
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandArgs
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.CommandElement
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers
import java.lang.StringBuilder

class SpongeCommandSet(val commandSpecs: MutableList<CommandSpec>, val alias: Array<String>)

fun CommandNexus.registerSpongeCommands(): SpongeCommandSet = let { nexus ->
    val commandSpecs = mutableListOf<CommandSpec>()
    for (alia in nexus.alias) {
        commandSpecs.add(
            CommandSpec.builder()
            // 需要测试
            .arguments(
                GenericArguments.remainingJoinedStrings(Text.of("help|<other>...")).let {
                    object : CommandElement(Text.of("help|<other>...")) {
                        // Copied in sponge RemainingJoinedStringsCommandElement's parseValue
                        override fun parseValue(source: CommandSource, args: CommandArgs): Any {
                            args.next()
                            val ret = args.raw.substring(args.rawPosition)
                            while (args.hasNext()) {
                                args.next()
                            }
                            return ret
                        }

                        override fun complete(src: CommandSource, args: CommandArgs, context: CommandContext) =
                            nexus.tabComplete(src.toGlobalSender(), mutableListOf(alia), args.all.toMutableList()) ?: mutableListOf()

                        override fun getUsage(src: CommandSource): Text {
                            plugin.server.schedule.task(plugin, TaskType.SYNC) {
                                src.toGlobalSender().also { sender ->
                                    helpGenerator.translateTexts(nexus, mutableListOf(alia), mutableListOf())[0].forEach { sender.sendMessage(it.text) }
                                }
                            }
                            return Text.of("")
                        }

                    }
                }
            )
            .executor { src, args ->
                nexus.execute(src.toGlobalSender(), mutableListOf(alia), args.getAll<String>("args").toMutableList())
                CommandResult.success()
            }
            .also {
                if (nexus.sendLimit.permission)
                    it.permission("${nexus.alias[0]}.use")
                Sponge.getCommandManager().register(nexus.plugin.toSpongePlugin(), it.build(), alia)
            }.build()
        )
    }

    SpongeCommandSet(commandSpecs, nexus.alias)
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) { player.simulateChat(TextSerializers.FORMATTING_CODE.deserialize(message), Cause.builder().reset().build(EventContext.builder().reset().build())) }
        override val name: String = player.name
        override fun sendMessage(message: String)  = player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message))
        override fun sendMessage(messages: Array<String>) = player.sendMessage(TextTemplate.of(messages))
        override fun hasPermission(name: String): Boolean = player.hasPermission(name)

    }
}
fun CommandSource.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is Player)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = sender.name
            override fun sendMessage(message: String) = sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message))
            override fun sendMessage(messages: Array<String>) = sender.sendMessage(TextTemplate.of(messages))
            override fun hasPermission(name: String) = sender.hasPermission(name)
        }

}

fun GlobalPlayer.toSpongePlayer(): Player = Sponge.getServer().getPlayer(this.uniqueId).get()
fun GlobalSender.toSpongeSender(): CommandSource = Sponge.getServer().getPlayer(this.name).let { if (it.isPresent) it.get() else Sponge.getServer().console }