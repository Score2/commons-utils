package me.scoretwo.utils.sponge.command

import me.scoretwo.utils.command.sender.GlobalPlayer
import me.scoretwo.utils.command.sender.GlobalSender
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.text.TextTemplate
import java.util.*
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers
import org.spongepowered.api.text.title.Title.reset


object SpongeCommand {
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