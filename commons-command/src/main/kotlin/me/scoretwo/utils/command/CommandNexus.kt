package me.scoretwo.utils.command

import me.scoretwo.utils.command.helper.DefaultHelpGenerator
import me.scoretwo.utils.command.language.DefaultCommandLanguage
import me.scoretwo.utils.plugin.GlobalPlugin

open class CommandNexus(plugin: GlobalPlugin, alias: Array<String>, sendLimit: SendLimit = SendLimit.ALL): SubCommand(plugin, alias, sendLimit) {



}