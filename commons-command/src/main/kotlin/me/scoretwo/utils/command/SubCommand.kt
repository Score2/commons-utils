package me.scoretwo.utils.command

import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import net.md_5.bungee.api.chat.BaseComponent

abstract class SubCommand(val alias: Array<String>,
                          var helpGenerator: HelpGenerator = DefaultHelpGenerator("ExamplePlugin", "1.0")
) {

    open var subCommands = mutableListOf<SubCommand>()

    fun execute(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        if (!sender.hasPermission("${toNode(parents)}.use")) {
            return true
        }

        if (args.isEmpty()) {
            helpGenerator.translateTexts(parents, args)[0].forEach { sender.sendMessage(it.text) }
            return true
        }

        val subCommand = findSubCommand(args[0]) ?: return execute(
            sender,
            parents.also {
                it.add(args[0])
            },
            mutableListOf<String>().also {
                for (i in 1 until args.size) {
                    it.add(args[i])
                }
            }
        )

        return subCommand.executed(sender, parents, args)
    }


    /**
     * subCommand list + tabCompleted list to return
     * 方法参考 FastScript
     */
    fun tabComplete(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>? {
        if (!sender.hasPermission("${toNode(parents)}.use")) {
            return null
        }

        if (args.isEmpty()) {
            val commandNames = mutableListOf<String>()

            subCommands.forEach { commandNames.addAll(it.alias) }

            commandNames.addAll(tabCompleted(sender, parents, args))

            return commandNames
        }

        for (subCommand in subCommands) {
            if (subCommand.alias.contains(args[0])) {

                val parentsEditor = parents.toMutableList()
                parentsEditor.add(args[0])
                val rearArgs = mutableListOf<String>()

                for (i in 1 until args.size) {
                    rearArgs.add(args[i])
                }

                return subCommand.tabComplete(sender, parentsEditor, rearArgs)

            }
        }

        return null
    }

    open fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        return false
    }

    open fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String> {
        return mutableListOf()
    }


    fun findSubCommand(alia: String): SubCommand? {
        subCommands.forEach { subCommand ->
            if (subCommand.alias.contains(alia))
                return subCommand
        }
        return null
    }

/*
    fun Any.sendMessage(string: String) {
        processor.sendMessage(this, string)
    }

    fun Any.sendMessage(component: BaseComponent) {
        processor.sendMessage(this, component)
    }

    fun Any.hasPermission(permission: String): Boolean {
        return processor.hasPermission(this, permission)
    }

    fun Any.getName(): String {
        return processor.getName(this)
    }*/

    private fun toNode(array: MutableList<String>): String {
        var string = ""
        array.forEach { string += "${it}." }
        return string.substring(0, string.length - 1)
    }

}

enum class SendLimit { PLAYER, CONSOLE, ALL, PERMISSION }