package me.scoretwo.utils.command.executor

import me.scoretwo.utils.sender.GlobalSender

interface TabExecutor {
    fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>?
}