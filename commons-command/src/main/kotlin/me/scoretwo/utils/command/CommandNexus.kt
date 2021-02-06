package me.scoretwo.utils.command

import me.scoretwo.utils.plugin.GlobalPlugin

open class CommandNexus(val plugin: GlobalPlugin, alias: Array<String>): SubCommand(alias) {

    override val nexus: CommandNexus = this

}