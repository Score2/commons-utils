package me.scoretwo.utils.command

import net.md_5.bungee.api.chat.BaseComponent

interface CommandProcessor {

    fun conversionSender()

    fun sendMessage(string: String)

    fun hasPermission(permission: String): Boolean

    fun sendMessage(component: BaseComponent)

    fun getName(): String

}