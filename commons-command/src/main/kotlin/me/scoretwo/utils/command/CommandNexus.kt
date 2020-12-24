package me.scoretwo.utils.command

open class CommandNexus(alias: Array<String>): SubCommand(alias) {

    init {

    }

    override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        return true
    }

    override fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }

}