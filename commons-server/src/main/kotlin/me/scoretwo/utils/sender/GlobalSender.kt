package me.scoretwo.utils.sender

import me.scoretwo.utils.server.globalServer

interface GlobalSender {

    val name: String

    fun sendMessage(text: String, placeholders: Map<String, String>) {
        var rawText = text
        placeholders.forEach {
            rawText = rawText.replace(it.key, it.value)
        }
    }

    fun sendMessage(text: String = "")

    fun sendMessage(texts: Array<String>)

    fun hasPermission(name: String): Boolean

    fun toPlayer() = globalServer.getPlayer(name).let {
        if (it.isPresent) it.get() else null
    }

    fun isPlayer() = globalServer.isOnlinePlayer(name)
}