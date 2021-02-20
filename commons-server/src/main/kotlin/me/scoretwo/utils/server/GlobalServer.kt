package me.scoretwo.utils.server

import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.task.GlobalSchedule
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import java.util.*

interface GlobalServer {

    val name: String
    val brand: ServerBrand
    val version: String
    val schedule: GlobalSchedule
    val console: GlobalSender

    fun getPlayer(username: String): Optional<GlobalPlayer>
    fun getPlayer(uniqueId: UUID): Optional<GlobalPlayer>

    fun getOnlinePlayers(): Collection<GlobalPlayer>
    fun isOnlinePlayer(username: String): Boolean = getPlayer(username).isPresent
    fun isOnlinePlayer(player: GlobalPlayer): Boolean
    fun isOnlinePlayer(uniqueId: UUID): Boolean

    fun broadcast(text: String) = broadcast(TextComponent(text))
    fun broadcast(texts: Array<String>) = broadcast(texts.joinToString(""))
    fun broadcast(text: BaseComponent) = broadcast(arrayOf(text))
    fun broadcast(texts: Array<BaseComponent>) = getOnlinePlayers().forEach { it.sendMessage(*texts) }

}
fun isGlobalServerAvailable() = ::globalServer.isInitialized
lateinit var globalServer: GlobalServer
