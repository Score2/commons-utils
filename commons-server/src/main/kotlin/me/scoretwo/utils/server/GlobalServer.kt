package me.scoretwo.utils.server

import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.task.GlobalSchedule
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

}
fun isGlobalServerAvailable() = ::globalServer.isInitialized
lateinit var globalServer: GlobalServer
