package me.scoretwo.utils.command

open class CommandNexus(alias: Array<String>, processor: CommandProcessor): SubCommand(alias, processor) {

    init {

    }

    override fun executed(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
        return true
    }

    override fun tabCompleted(sender: GlobalSender, parents: Array<String>, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

}