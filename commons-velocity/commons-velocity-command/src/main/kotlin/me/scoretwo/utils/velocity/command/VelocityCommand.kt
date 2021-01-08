package me.scoretwo.utils.velocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import java.util.*

object VelocityCommand {
}
fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) { player.simulateChat(TextSerializers.FORMATTING_CODE.deserialize(message), Cause.builder().reset().build(EventContext.builder().reset().build())) }
        override val name: String = player.username
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

fun GlobalPlayer.toVelocityPlayer(): Player = ProxyServer.getPlayer(this.uniqueId).get()
fun GlobalSender.toVelocitySender(): CommandSource = Velocity.getServer().getPlayer(this.name).let { if (it.isPresent) it.get() else Sponge.getServer().console }