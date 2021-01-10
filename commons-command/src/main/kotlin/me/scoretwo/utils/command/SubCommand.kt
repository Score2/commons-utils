package me.scoretwo.utils.command

import me.scoretwo.utils.command.SendLimit.*
import me.scoretwo.utils.command.executor.CommandExecutor
import me.scoretwo.utils.command.executor.TabExecutor
import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import me.scoretwo.utils.command.language.CommandLanguage
import me.scoretwo.utils.command.language.DefaultCommandLanguage
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender

abstract class SubCommand(val plugin: GlobalPlugin,
                          val alias: Array<String>,
                          val sendLimit: SendLimit = ALL,
                          val language: CommandLanguage = DefaultCommandLanguage(),
                          var helpGenerator: HelpGenerator = DefaultHelpGenerator("ExamplePlugin", "1.0")
): CommandExecutor, TabExecutor {

    open var subCommands = mutableListOf<SubCommand>()

    fun execute(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        when (sendLimit) {
            PLAYER -> {
                if (sender !is GlobalPlayer) {
                    sender.sendMessage(language.COMMAND_ONLY_PLAYER)
                    return true
                }
            }
            ALL -> {
            }
            CONSOLE -> {
                if (sender is GlobalPlayer) {
                    sender.sendMessage(language.COMMAND_ONLY_CONSOLE)
                    return true
                }
            }
        }
        if (sendLimit.permission && !sender.hasPermission("${toNode(parents)}.use")) {
            sender.sendMessage(language.COMMAND_NO_PERMISSION)
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
        when (sendLimit) {
            PLAYER -> {
                if (sender !is GlobalPlayer) {
                    return null
                }
            }
            ALL -> {
            }
            CONSOLE -> {
                if (sender is GlobalPlayer) {
                    return null
                }
            }
        }
        if (sendLimit.permission && !sender.hasPermission("${toNode(parents)}.use")) {
            return null
        }

        if (args.isEmpty()) {
            val commandNames = mutableListOf<String>()

            subCommands.forEach { commandNames.addAll(it.alias) }

            commandNames.addAll(tabCompleted(sender, parents, args) ?: mutableListOf())

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

    override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
        return true
    }

    override fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>? {
        return null
    }

    fun findSubCommand(alia: String): SubCommand? {
        subCommands.forEach { subCommand ->
            if (subCommand.alias.contains(alia))
                return subCommand
        }
        return null
    }

    private fun toNode(array: MutableList<String>): String {
        var string = ""
        array.forEach { string += "${it}." }
        return string.substring(0, string.length - 1)
    }

}

class CommandBuilder {

    var subCommand: SubCommand? = null
    var plugin: GlobalPlugin? = null
    var alias: Array<String>? = null

    fun alias(vararg alias: String) {
        this.alias = arrayOf(*alias)

    }

    private fun initSubCommand() {
        if (subCommand != null && alias != null && plugin != null) {
            subCommand = object : SubCommand(plugin!!, alias!!) {
                override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
                    return true
                }
                override fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>? {
                    return null
                }
            }
        }
    }

    companion object {
        fun builder() = CommandBuilder()
    }
}

enum class SendLimit {
    PLAYER,
    CONSOLE,
    ALL;

    var permission = true

    fun permission(need: Boolean): SendLimit = this.also {
        it.permission = need
    }
}