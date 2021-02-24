package me.scoretwo.utils.command.executor

import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.globalServer

/**
 * @author Score2
 * @date 2021/2/6 15:15
 *
 * @project commons-utils
 */
class OnlinePlayerTabExecutor: TabExecutor {
    override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>) =
        mutableListOf<String>().also { players ->
            globalServer.getOnlinePlayers().forEach {
                players.add(it.name)
            }
        }
}