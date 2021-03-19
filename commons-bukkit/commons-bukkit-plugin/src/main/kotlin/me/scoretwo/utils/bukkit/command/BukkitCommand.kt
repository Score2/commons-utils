package me.scoretwo.utils.bukkit.command

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import java.util.*

val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap

fun CommandNexus.registerBukkitCommands(): Command = this.let { nexus ->
    if (nexus.shadowInstance != null || nexus.shadowInstance is Command) {
        throw Exception("This CommandNexus has already been registered!")
    }
    object : Command(nexus.alias[0], "", "/${nexus.alias[0]}", nexus.alias.slice(1 until nexus.alias.size)) {
        override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
            nexus.execute(sender.toGlobalSender(), mutableListOf(label), args.toMutableList())
            return true
        }
        override fun tabComplete(sender: CommandSender, label: String, args: Array<out String>): MutableList<String> {
            return nexus.tabComplete(sender.toGlobalSender(), mutableListOf(label), args.toMutableList())
        }
    }.also {
        bukkitCommandMap.register(alias[0], it)
        nexus.shadowInstance = it
    }
}

fun CommandNexus.unregisterBukkitCommand(): Boolean = this.let { nexus ->
    if (nexus.shadowInstance == null || nexus.shadowInstance !is Command) {
        return@let false
    }
    val result = (nexus.shadowInstance as Command).unregister(bukkitCommandMap)
    nexus.shadowInstance = null
    return@let result
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) = player.chat(message)
        override val name: String = player.name
        override fun sendMessage(text: String) = player.sendMessage(text)
        override fun sendMessage(texts: Array<String>) = player.sendMessage(texts)
        override fun sendMessage(text: BaseComponent) = player.spigot().sendMessage(text)
        override fun sendMessage(vararg texts: BaseComponent) = player.spigot().sendMessage(*texts)
        override fun hasPermission(name: String) = player.hasPermission(name)
    }
}
fun CommandSender.toGlobalSender(): GlobalSender = this.let { sender ->
    if (sender is Player)
        sender.toGlobalPlayer()
    else
        object : GlobalSender {
            override val name: String = sender.name
            override fun sendMessage(text: String) = sender.sendMessage(text)
            override fun sendMessage(texts: Array<String>) = sender.sendMessage(texts)
            override fun hasPermission(name: String): Boolean = sender.hasPermission(name)
            override fun sendMessage(text: BaseComponent) = try {
                sender.spigot().sendMessage(text)
            } catch (t: Throwable) {
                sendMessage(text.toPlainText())
            }
            override fun sendMessage(vararg texts: BaseComponent) = try {
                sender.spigot().sendMessage(*texts)
            } catch (t: Throwable) {
                sendMessage(texts.joinToString("") { it.toPlainText() })
            }
        }
}


fun GlobalPlayer.toBukkitPlayer(): Player = Bukkit.getPlayer(this.uniqueId)!!
fun GlobalSender.toBukkitSender(): CommandSender = Bukkit.getPlayer(this.name) ?: Bukkit.getConsoleSender()