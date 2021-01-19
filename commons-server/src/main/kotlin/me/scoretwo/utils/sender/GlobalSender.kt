package me.scoretwo.utils.sender

import me.scoretwo.utils.server.globalServer

interface GlobalSender {

    val name: String

    fun sendMessage(message: String)

    fun sendMessage(messages: Array<String>)

    fun hasPermission(name: String): Boolean

    fun toPlayer() = globalServer.getPlayer(name).let {
        if (it.isPresent) it.get() else null
    }

    fun isPlayer() = globalServer.isOnlinePlayer(name)
}