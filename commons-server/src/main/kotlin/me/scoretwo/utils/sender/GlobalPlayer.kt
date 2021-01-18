package me.scoretwo.utils.sender

import java.util.*

interface GlobalPlayer: GlobalSender {

    val uniqueId: UUID

    fun chat(message: String)

    override fun toPlayer() = this

    override fun isPlayer() = true
/*
    val viewDistance: Int

    fun sendTitle(title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int)
*/


}