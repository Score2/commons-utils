package me.scoretwo.utils.command.executor

import me.scoretwo.utils.sender.GlobalSender

interface CommandExecutor {
    fun execute(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean
}