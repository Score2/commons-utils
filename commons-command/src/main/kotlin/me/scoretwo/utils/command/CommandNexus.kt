package me.scoretwo.utils.command

import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalSender

open class CommandNexus(plugin: GlobalPlugin, alias: Array<String>): SubCommand(plugin, alias) {

    init {

    }

    override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        return true
    }

    override fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

}