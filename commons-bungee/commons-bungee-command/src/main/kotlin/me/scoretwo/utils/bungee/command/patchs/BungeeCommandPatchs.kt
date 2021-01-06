package me.scoretwo.utils.bungee.command.patchs

import me.scoretwo.utils.command.GlobalSender
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent

fun CommandSender.toGlobalSender(): GlobalSender {
    val upstream = this
    return object : GlobalSender {
        override val name: String = upstream.name

        override fun sendMessage(message: String) {
            upstream.sendMessage(TextComponent(message))
        }

        override fun sendMessage(messages: Array<String>) {
                        upstream.sendMessage(*mutableListOf<TextComponent>().also { tc -> messages.forEach {
                tc.add(TextComponent(it))
            }}.toTypedArray())
        }

        override fun hasPermission(name: String): Boolean {
            return upstream.hasPermission(name)
        }
    }
}

fun GlobalSender.toBungeeSender(): CommandSender {
    return ProxyServer.getInstance().getPlayer(this.name) ?: ProxyServer.getInstance().console
}