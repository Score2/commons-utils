package me.scoretwo.utils.command

import me.scoretwo.utils.command.executor.CommandExecutor
import me.scoretwo.utils.command.executor.TabExecutor
import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import me.scoretwo.utils.command.language.CommandLanguage
import me.scoretwo.utils.command.language.DefaultCommandLanguage
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalSender

open class CommandNexus(val plugin: GlobalPlugin, alias: Array<String>): SubCommand(alias) {

    override val nexus: CommandNexus = this

    override var commandExecutor = object : CommandExecutor {
        override fun execute(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {
            return true
        }
    }

    override var tabExecutor = object : TabExecutor {
        override fun tabComplete(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): MutableList<String>? {
            return null
        }
    }

}