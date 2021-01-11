package me.scoretwo.utils.command

import me.scoretwo.utils.command.SendLimit.*
import me.scoretwo.utils.command.executor.CommandExecutor
import me.scoretwo.utils.command.executor.TabExecutor
import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import me.scoretwo.utils.command.language.CommandLanguage
import me.scoretwo.utils.command.language.DefaultCommandLanguage
import me.scoretwo.utils.exceptions.CommandException
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender

abstract class SubCommand(val plugin: GlobalPlugin,
                          val alias: Array<String>,
                          var sendLimit: SendLimit = ALL,
                          var language: CommandLanguage = DefaultCommandLanguage(),
                          var helpGenerator: HelpGenerator = DefaultHelpGenerator("ExamplePlugin", "1.0")
): CommandExecutor, TabExecutor {

    open var subCommands = mutableListOf<SubCommand>()

    var commandExecutor = object : CommandExecutor {
        override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
            return true
        }
    }

    var tabExecutor = object : TabExecutor {
        override fun tabCompleted(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>? {
            return null
        }
    }

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

    fun builder() = CommandBuilder.builder()
        .plugin(plugin)
        .alias(*alias)
        .execute(commandExecutor)
        .tabComplete(tabExecutor)
        .limit(sendLimit)
        .language(language)
        .helpGenerator(helpGenerator)

}

class CommandBuilder {
    private var plugin: GlobalPlugin? = null
    private var alias: Array<String>? = null

    private var commandExecutor: CommandExecutor? = null
    private var tabExecutor: TabExecutor? = null

    private var sendLimit: SendLimit? = null
    private var language: CommandLanguage? = null
    private var helpGenerator: HelpGenerator? = null

    fun alias(alias: List<String>) = this.also { this.alias = alias.toTypedArray() }
    fun alias(vararg alias: String) = this.also { this.alias = arrayOf(*alias) }
    fun plugin(plugin: GlobalPlugin) = this.also { this.plugin = plugin }
    fun execute(commandExecutor: CommandExecutor) = this.also { this.commandExecutor = commandExecutor }
    fun tabComplete(tabExecutor: TabExecutor) = this.also { this.tabExecutor = tabExecutor }
    fun limit(limit: SendLimit) = this.also { this.sendLimit = limit }
    fun language(language: CommandLanguage) = this.also { this.language = language }
    fun helpGenerator(helpGenerator: HelpGenerator) = this.also { this.helpGenerator = helpGenerator }

    fun reset() = this.also {
        /*
        plugin = null
        alias = null
        commandExecutor = null
        tabExecutor = null
        sendLimit = null
        */
        javaClass.fields.forEach {
            it.isAccessible = true
            it.set(this, null)
        }
    }

    fun build(): SubCommand {
        plugin ?: throw CommandException("plugin", "null")
        alias ?: throw CommandException("alias", "null")
        if (alias!!.isEmpty()) throw CommandException("alias", "empty")

        return object : SubCommand(plugin!!, alias!!) {}.also {
            if (sendLimit != null) it.sendLimit = sendLimit!!
            if (helpGenerator != null) it.helpGenerator = helpGenerator!!
            if (language != null) it.language = language!!
            if (commandExecutor != null) it.commandExecutor = commandExecutor!!
            if (tabExecutor != null) it.tabExecutor = tabExecutor!!
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