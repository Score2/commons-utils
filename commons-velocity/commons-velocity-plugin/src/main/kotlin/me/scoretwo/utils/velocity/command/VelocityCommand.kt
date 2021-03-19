package me.scoretwo.utils.velocity.command

import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.velocity.server.proxyServer
import net.kyori.text.serializer.legacy.LegacyComponentSerializer
import java.util.*
import net.md_5.bungee.api.chat.BaseComponent

fun CommandNexus.registerVelocityCommands(): Command = let { nexus ->
    SimpleCommand { invocation ->
        nexus.execute(invocation.source().toGlobalSender(), mutableListOf(invocation.alias()), invocation.arguments().toMutableList())
    }.also {
        nexus.shadowInstance = it
        proxyServer.commandManager.register(proxyServer.commandManager.metaBuilder(nexus.alias[0]).aliases(*nexus.alias.sliceArray(1 until nexus.alias.size)).build(), it)
    }
}

fun CommandNexus.unregisterVelocityCommand(): Boolean = this.let { nexus ->
    if (nexus.shadowInstance == null || nexus.shadowInstance !is Command) {
        return@let false
    }
    proxyServer.commandManager.unregister(nexus.alias[0])
    nexus.shadowInstance = null
    return@let true
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) { player.spoofChatInput(message) }
        override val name: String = player.username
        override fun sendMessage(text: String) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(text))
        override fun sendMessage(texts: Array<String>) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(texts.joinToString("")))
        override fun sendMessage(text: BaseComponent) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(text.toPlainText()))
        override fun sendMessage(vararg texts: BaseComponent) = player.sendMessage(LegacyComponentSerializer.legacy().deserialize(texts.joinToString { it.toPlainText() }))
        override fun hasPermission(name: String): Boolean = player.hasPermission(name)

    }
}
fun CommandSource.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is Player)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = "CONSOLE"
            override fun sendMessage(text: String) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(text))
            override fun sendMessage(texts: Array<String>) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(texts.joinToString("")))
            override fun sendMessage(text: BaseComponent) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(text.toPlainText()))
            override fun sendMessage(vararg texts: BaseComponent) = sender.sendMessage(LegacyComponentSerializer.legacy().deserialize(texts.joinToString { it.toPlainText() }))
            override fun hasPermission(name: String) = sender.hasPermission(name)
        }

}

fun GlobalPlayer.toVelocityPlayer(): Player = proxyServer.getPlayer(this.uniqueId).get()
fun GlobalSender.toVelocitySender(): CommandSource = proxyServer.getPlayer(this.name).let { if (it.isPresent) it.get() else proxyServer.consoleCommandSource }
