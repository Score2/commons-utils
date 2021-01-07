package me.scoretwo.utils.server

import me.scoretwo.utils.sender.GlobalPlayer
import java.util.*

interface GlobalServer {

    fun getPlayer(username: String): Optional<GlobalPlayer>
    fun getPlayer(uuid: UUID): Optional<GlobalPlayer>

}