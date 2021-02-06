package me.scoretwo.utils.command.helper

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.SubCommand
import me.scoretwo.utils.plugin.PluginDescription
import net.md_5.bungee.api.chat.TextComponent

interface HelpGenerator {

    val nexus: CommandNexus

    // return page<lines>
    fun translateTexts(parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<TextComponent>>

}