package me.scoretwo.utils.command

import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.helper.HelpGenerator
import me.scoretwo.utils.command.language.CommandLanguage
import me.scoretwo.utils.command.language.DefaultCommandLanguage
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.server.globalServer

open class CommandNexus(
    plugin: GlobalPlugin,
    alias: Array<String>,
    sendLimit: SendLimit = SendLimit.ALL,
    language: CommandLanguage = DefaultCommandLanguage(),
    helpGenerator: HelpGenerator = DefaultHelpGenerator(plugin)
): SubCommand(plugin, alias, sendLimit, language, helpGenerator) {

    var shadowInstance: Any? = null

    fun register() = globalServer.commandMap.register(this)

    fun unregister() = globalServer.commandMap.unregister(this)

}