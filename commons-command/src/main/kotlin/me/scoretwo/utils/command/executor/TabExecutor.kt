package me.scoretwo.utils.command.executor

import me.scoretwo.utils.sender.GlobalSender

interface TabExecutor {
    fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>): MutableList<String>?
}