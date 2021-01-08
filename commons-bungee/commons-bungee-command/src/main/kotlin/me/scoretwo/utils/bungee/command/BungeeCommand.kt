package me.scoretwo.utils.bungee.command

import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object BungeeCommand {
}

fun ProxiedPlayer.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) = player.chat(message)
        override val name: String = player.name
        override fun sendMessage(message: String) = player.sendMessage(TextComponent(message))
        override fun sendMessage(messages: Array<String>) = player.sendMessage(*mutableListOf<TextComponent>().also { texts -> messages.forEach { texts.add(TextComponent(it)) } }.toTypedArray())
        override fun hasPermission(name: String) = player.hasPermission(name)
    }
}
fun CommandSender.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is ProxiedPlayer)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = sender.name
            override fun sendMessage(message: String) = sender.sendMessage(TextComponent(message))
            override fun sendMessage(messages: Array<String>) = sender.sendMessage(*mutableListOf<TextComponent>().also { texts -> messages.forEach { texts.add(TextComponent(it)) } }.toTypedArray())
            override fun hasPermission(name: String): Boolean = sender.hasPermission(name)
        }
}

fun GlobalPlayer.toBungeePlayer(): ProxiedPlayer = ProxyServer.getInstance().getPlayer(this.uniqueId)!!
fun GlobalSender.toBungeeSender(): CommandSender = ProxyServer.getInstance().getPlayer(this.name) ?: ProxyServer.getInstance().console