package me.scoretwo.utils.bukkit.command

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.globalServer
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import java.util.*

val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap

fun CommandNexus.registerBukkitCommands(): Command = this.let { nexus ->
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
    }
}

fun Player.toGlobalPlayer(): GlobalPlayer = this.let { player ->
    object : GlobalPlayer {
        override val uniqueId: UUID = player.uniqueId
        override fun chat(message: String) = player.chat(message)
        override val name: String = player.name
        override fun sendMessage(message: String) = player.sendMessage(message)
        override fun sendMessage(messages: Array<String>) = player.sendMessage(messages)
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
            override fun sendMessage(message: String) = sender.sendMessage(message)
            override fun sendMessage(messages: Array<String>) = sender.sendMessage(messages)
            override fun hasPermission(name: String): Boolean = sender.hasPermission(name)
            override fun sendMessage(text: BaseComponent) = sender.spigot().sendMessage(text)
            override fun sendMessage(vararg texts: BaseComponent) = sender.spigot().sendMessage(*texts)
        }
}


fun GlobalPlayer.toBukkitPlayer(): Player = Bukkit.getPlayer(this.uniqueId)!!
fun GlobalSender.toBukkitSender(): CommandSender = Bukkit.getPlayer(this.name) ?: Bukkit.getConsoleSender()