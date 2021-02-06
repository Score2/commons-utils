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
import org.apache.commons.lang.StringUtils
import java.util.stream.Collectors

import java.util.Arrays




abstract class SubCommand(
    val plugin: GlobalPlugin,
    open val alias: Array<String>,
    open var sendLimit: SendLimit = ALL
) {

    open var subCommands = mutableListOf<SubCommand>()

    open var language: CommandLanguage = DefaultCommandLanguage()
    open var helpGenerator: HelpGenerator = DefaultHelpGenerator(plugin)

    open var tabExecutor = object : TabExecutor {
        override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>): MutableList<String>? {
            return null
        }
    }

    open var commandExecutor = object : CommandExecutor {
        override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
            return true
        }
    }

    fun registerBuilder() = CommandBuilder.builder()
        .plugin(plugin)
        .helpGenerator(helpGenerator)
        .language(language)
        .limit(sendLimit)

    fun register(command: SubCommand) = subCommands.add(command)

    fun unregister(command: SubCommand) = subCommands.remove(command)
    fun unregister(alia: String): Boolean {
        for (command in subCommands.toTypedArray()) {
            if (command.alias.contains(alia)) {
                subCommands.remove(command)
                return true
            }
        }
        return false
    }

    open fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>) =
        commandExecutor.execute(sender, parents, args)

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

        if (sendLimit.permission && !sender.hasPermission(toNode(parents).let { if (parents.size == 1) "${alias[0]}.use" else "$it.use" })) {
            sender.sendMessage(language.COMMAND_NO_PERMISSION)
            return true
        }

        if (args.isEmpty()) {
            helpGenerator.translateTexts(parents, args)[0].forEach { sender.sendMessage(it.text) }
            return true
        }

        if (args[0].toLowerCase() == "help") {
            val helps = helpGenerator.translateTexts(parents, args)
            if (args.size >= 1) {
                helps[0].forEach { sender.sendMessage(it.text) }
                return true
            }
            var page = args[1].let { if (StringUtils.isNumeric(it)) it.toInt() else 0 }

            if (!(0..helps.size).contains(page)) {
                page = 0
            }

            helps[page].forEach { sender.sendMessage(it.text) }
            return true
        }

        val subCommand = findSubCommand(args[0]) ?: return execute(sender, parents.toTypedArray(), args.toTypedArray())

        return subCommand.execute(
            sender,
            parents.also { it.add(args[0]) },
            mutableListOf<String>().also { (1 until args.size).forEach { i -> it.add(args[i]) } }
        )
    }

    open fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>): MutableList<String>? =
        tabExecutor.tabComplete(sender, parents, args)

    /**
     * subCommand list + tabCompleted list to return.
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
        if (sendLimit.permission && !sender.hasPermission(toNode(parents).let { if (parents.size == 1) "${alias[0]}.use" else "$it.use" })) {
            return null
        }

        if (args.isEmpty()) {
            val commandAlias = mutableListOf<String>()

            subCommands.forEach { commandAlias.addAll(it.alias) }

            commandAlias.addAll(tabComplete(sender, parents.toTypedArray(), args.toTypedArray()) ?: mutableListOf())

            return commandAlias
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

    fun findKeywordIndex(key: String, list: MutableList<String>) = mutableListOf<String>().also { result -> list.forEach { if (it.startsWith(key)) result.add(it) } }

    fun findSubCommand(alia: String): SubCommand? {
        for (subCommand in subCommands) {
            if (subCommand.alias.contains(alia))
                return subCommand
        }
        return null
    }

    fun toNode(array: MutableList<String>): String {
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
        .subCommand(subCommands)

}

