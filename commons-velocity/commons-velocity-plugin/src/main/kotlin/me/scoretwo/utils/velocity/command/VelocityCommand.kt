package me.scoretwo.utils.velocity.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.velocity.server.proxyServer
import net.kyori.text.serializer.legacy.LegacyComponentSerializer
import java.util.*
import net.kyori.adventure.text.format.NamedTextColor

fun CommandNexus.registerVelocityCommands(): Command = let { nexus ->
    SimpleCommand { invocation ->
        nexus.execute(invocation.source().toGlobalSender(), mutableListOf(invocation.alias()), invocation.arguments().toMutableList())
    }.also {
        proxyServer.commandManager.register(proxyServer.commandManager.metaBuilder(nexus.alias[0]).aliases(*nexus.alias.sliceArray(1..nexus.alias.size)).build(), it)
    }
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) { player.spoofChatInput(message) }
        override val name: String = player.username
        override fun sendMessage(message: String) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(message))
        override fun sendMessage(messages: Array<String>) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(messages.toString()))
        override fun hasPermission(name: String): Boolean = player.hasPermission(name)

    }
}
fun CommandSource.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is Player)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = "CONSOLE"
            override fun sendMessage(message: String) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(message))
            override fun sendMessage(messages: Array<String>) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(messages.toString()))
            override fun hasPermission(name: String) = sender.hasPermission(name)
        }

}

fun GlobalPlayer.toVelocityPlayer(): Player = proxyServer.getPlayer(this.uniqueId).get()
fun GlobalSender.toVelocitySender(): CommandSource = proxyServer.getPlayer(this.name).let { if (it.isPresent) it.get() else proxyServer.consoleCommandSource }
