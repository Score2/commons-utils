package me.scoretwo.utils.sponge.command

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.sponge.plugin.toSpongePlugin
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.text.TextTemplate
import java.util.*
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers

class SpongeCommandSet(val commandSpecs: MutableList<CommandSpec>, val alias: Array<String>)

fun CommandNexus.registerSpongeCommands(): SpongeCommandSet = let { nexus ->
    val commandSpecs = mutableListOf<CommandSpec>()
    for (alia in nexus.alias) {
        commandSpecs.add(CommandSpec.builder()
            // execute部分已完美运行, tabComplete 还未实现
            .executor { src, args ->
                nexus.execute(src.toGlobalSender(), mutableListOf(alia), args.getAll<String>("args").toMutableList())
                CommandResult.success()
            }
            .build().also {
                Sponge.getCommandManager().register(nexus.plugin.toSpongePlugin(), it, alia)
            }
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