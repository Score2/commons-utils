package me.scoretwo.utils.command

open class CommandNexus(vararg alias: String, processor: CommandProcessor): SubCommand(alias, processor) {

    init {

    }

    override fun executed(sender: Any, parents: Array<String>, label: String, args: Array<String>): Boolean {
        return true
    }

    override fun tabCompleted(sender: Any, parents: Array<String>, label: String, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

}