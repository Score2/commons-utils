package me.scoretwo.utils.bukkit.command.patchs

import me.scoretwo.utils.command.GlobalSender
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap

val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap

fun CommandSender.toGlobalSender(): GlobalSender {
    val upstream = this
    return object : GlobalSender {
        override val name: String = upstream.name

        override fun sendMessage(message: String) {
            upstream.sendMessage(message)
        }

        override fun sendMessage(messages: Array<String>) {
            upstream.sendMessage(messages)
        }

        override fun hasPermission(name: String): Boolean {
            return upstream.hasPermission(name)
        }
    }
}

fun GlobalSender.toBukkitSender(): CommandSender {
    return Bukkit.getPlayer(this.name) ?: Bukkit.getConsoleSender()
}