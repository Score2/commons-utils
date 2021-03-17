package me.scoretwo.utils.nukkit.command

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.getOrNull
import net.md_5.bungee.api.chat.BaseComponent
import java.util.*

/**
 * @author 83669
 * @date 2021/3/17 20:38
 *
 * @project commons-utils
 */

fun CommandNexus.registerNukkitCommands(): Command = this.let { nexus ->
    if (nexus.shadowInstance != null || nexus.shadowInstance is Command) {
        throw Exception("This CommandNexus has already been registered!")
    }
    object : Command(nexus.alias[0], "", "/${nexus.alias[0]}", nexus.alias.sliceArray(1 until nexus.alias.size)) {

        override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
            nexus.execute(sender.toGlobalSender(), mutableListOf(label), args.toMutableList())
            return true
        }

    }
}

fun CommandNexus.unregisterNukkitCommand(): Boolean = this.let { nexus ->
    if (nexus.shadowInstance == null || nexus.shadowInstance !is Command) {
        return@let false
    }
    val result = (nexus.shadowInstance as Command).unregister(Server.getInstance().commandMap)
    nexus.shadowInstance = null
    return@let result
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) { player.chat(message) }
        override val name: String = player.name
        override fun sendMessage(text: String) = player.sendMessage(text)
        override fun sendMessage(texts: Array<String>) = player.sendMessage(texts.joinToString(""))
        override fun sendMessage(text: BaseComponent) = player.sendMessage(text.toPlainText())
        override fun sendMessage(vararg texts: BaseComponent) = player.sendMessage(texts.joinToString("") { it.toPlainText() })
        override fun hasPermission(name: String) = player.hasPermission(name)
    }
}
fun CommandSender.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is Player)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = sender.name
            override fun hasPermission(name: String): Boolean = sender.hasPermission(name)
            override fun sendMessage(text: String) = sender.sendMessage(text)
            override fun sendMessage(texts: Array<String>) = sender.sendMessage(texts.joinToString(""))
            override fun sendMessage(text: BaseComponent) = sender.sendMessage(text.toPlainText())
            override fun sendMessage(vararg texts: BaseComponent) = sender.sendMessage(texts.joinToString("") { it.toPlainText() })
        }
}

fun GlobalPlayer.toNukkitPlayer(): Player = Server.getInstance().getPlayer(this.uniqueId).get()
fun GlobalSender.toNukkitSender(): CommandSender = Server.getInstance().getPlayer(this.name) ?: Server.getInstance().consoleSender