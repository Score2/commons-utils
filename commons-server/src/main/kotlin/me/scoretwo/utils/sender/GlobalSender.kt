package me.scoretwo.utils.sender

interface GlobalSender {

    val name: String

    fun sendMessage(message: String)

    fun sendMessage(messages: Array<String>)

    fun hasPermission(name: String): Boolean

    fun isPlayer(): Boolean

    fun toPlayer(): GlobalPlayer?
}