package me.scoretwo.utils.command

import me.scoretwo.utils.command.executor.CommandExecutor
import me.scoretwo.utils.command.executor.Executors
import me.scoretwo.utils.command.executor.TabExecutor
import me.scoretwo.utils.command.helper.HelpGenerator
import me.scoretwo.utils.command.language.CommandLanguage
import me.scoretwo.utils.exceptions.CommandException
import me.scoretwo.utils.plugin.GlobalPlugin

open class CommandBuilder {
    private var alias: Array<String>? = null
    private var plugin: GlobalPlugin? = null

    private var executors: Executors? = null
    private var commandExecutor: CommandExecutor? = null
    private var tabExecutor: TabExecutor? = null

    private var sendLimit: SendLimit? = null
    private var language: CommandLanguage? = null
    private var helpGenerator: HelpGenerator? = null
    private var description: String? = null
    private val customCommands: MutableMap<String, Pair<Array<String>?, String>> = mutableMapOf()

    private val subCommands = mutableListOf<SubCommand>()

    fun plugin(plugin: GlobalPlugin?) = this.also { this.plugin = plugin }
    fun alias(alias: List<String>) = this.also { this.alias = alias.toTypedArray() }
    fun alias(vararg alias: String) = this.also { this.alias = arrayOf(*alias) }

    fun executor(executors: Executors?) = this.also { this.executors = executors }
    fun execute(commandExecutor: CommandExecutor?) = this.also { this.commandExecutor = commandExecutor }
    fun tabComplete(tabExecutor: TabExecutor?) = this.also { this.tabExecutor = tabExecutor }

    fun limit(limit: SendLimit?) = this.also { this.sendLimit = limit }
    fun language(language: CommandLanguage?) = this.also { this.language = language }
    fun helpGenerator(helpGenerator: HelpGenerator?) = this.also { this.helpGenerator = helpGenerator }
    fun description(description: String?) = this.also { this.description = description }
    fun customCommands(customCommands: MutableMap<String, Pair<Array<String>?, String>>) = this.also { this.customCommands.putAll(customCommands) }
    fun customCommand(commandName: String, commandMoreArgs: Array<String>? = null, commandDescription: String = "Not more...") = this.also { this.customCommands[commandName] = Pair(commandMoreArgs, commandDescription) }
    fun clearCustomCommands() = this.also { customCommands.clear() }

    fun subCommand(builder: CommandBuilder) = this.also { subCommands.add(builder.build()) }
    fun subCommand(subCommand: SubCommand) = this.also { subCommands.add(subCommand) }
    fun subCommand(subCommands: MutableList<SubCommand>) = this.also { subCommands.addAll(subCommands) }
    fun clearSubCommands() = this.also { this.subCommands.clear() }

    fun nextBuilder() = builder()
        .plugin(plugin)
        .language(language)
        .helpGenerator(helpGenerator)
        .limit(sendLimit)

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

    fun build(): SubCommand = this.let { builder ->
        if (alias!!.isEmpty()) throw CommandException("alias", "empty")

        return@let object : SubCommand(plugin!!, alias!!) {
            override var commandExecutor = let {
                if (builder.executors != null)
                    builder.executors!!
                else
                    builder.commandExecutor ?: super.commandExecutor
            }
            override var tabExecutor = let {
                if (builder.executors != null)
                    builder.executors!!
                else
                    builder.tabExecutor ?: super.tabExecutor
            }

            override var customCommands = builder.customCommands
        }.also {
            if (description != null) it.description = description!!
            if (sendLimit != null) it.sendLimit = sendLimit!!
            if (helpGenerator != null) it.helpGenerator = helpGenerator!!
            if (language != null) it.language = language!!
            it.subCommands.addAll(subCommands)
        }
    }

    companion object {
        fun builder() = CommandBuilder()
    }
}