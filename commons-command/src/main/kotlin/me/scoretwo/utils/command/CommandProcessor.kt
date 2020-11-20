package me.scoretwo.utils.command

import net.md_5.bungee.api.chat.BaseComponent

interface CommandProcessor {

    fun sendMessage(sender: Any, string: String)

    fun hasPermission(sender: Any, permission: String): Boolean

    fun sendMessage(sender: Any, component: BaseComponent)

    fun getName(sender: Any): String

}