package me.scoretwo.utils.command.executor

import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.globalServer

/**
 * @author 83669
 * @date 2021/2/6 15:15
 *
 * @project commons-utils
 */
class OnlinePlayerTabExecutor: TabExecutor {
    override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>): MutableList<String> {
        val players = mutableListOf<String>()
        globalServer.getOnlinePlayers().forEach {
            players.add(it.name)
        }
        return players
    }
}