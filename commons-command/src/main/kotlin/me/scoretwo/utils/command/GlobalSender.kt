package me.scoretwo.utils.command

interface GlobalSender {

    val name: String

    fun sendMessage(message: String)

    fun sendMessage(messages: Array<String>)

    fun hasPermission(name: String): Boolean
}