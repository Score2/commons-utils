package me.scoretwo.utils.command

import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import net.md_5.bungee.api.chat.BaseComponent

abstract class SubCommand(val alias: Array<out String>,
                          private val processor: CommandProcessor,
                          var helpGenerator: HelpGenerator = DefaultHelpGenerator("ExamplePlugin", "1.0")
) {

    open var subCommands = mutableListOf<SubCommand>()

    fun execute(sender: Any, parents: Array<String>, args: Array<String>): Boolean {
        if (!sender.hasPermission("${toNode(parents)}.use")) {
            return true
        }

        if (args.isEmpty()) {
            helpGenerator.translateTexts(parents, args)[0].forEach { sender.sendMessage(it) }
            return true
        }

        parents[parents.size + 1] = args[0]
        val rearArgs = arrayOf<String>()

        for (i in 1 until args.size) {
            rearArgs[i - 1] = args[i]
        }

        val subCommand = findSubCommand(args[0]) ?: return executed(sender, parents, rearArgs)

        return subCommand.execute(sender, parents, rearArgs)
    }

    fun tabComplete(sender: Any, parents: Array<String>, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }

    open fun executed(sender: Any, parents: Array<String>, args: Array<String>): Boolean {
        return false
    }

    open fun tabCompleted(sender: Any, parents: Array<String>, args: Array<String>): MutableList<String> {
        return mutableListOf()
    }


    fun findSubCommand(alia: String): SubCommand? {
        subCommands.forEach { subCommand ->
            if (subCommand.alias.contains(alia))
                return subCommand
        }
        return null
    }


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
    }

    private fun toNode(array: Array<String>): String {
        var string = ""
        array.forEach { string += "${it}." }
        return string.substring(0, string.length - 1)
    }

}

enum class SendLimit { PLAYER, CONSOLE, ALL, PERMISSION }