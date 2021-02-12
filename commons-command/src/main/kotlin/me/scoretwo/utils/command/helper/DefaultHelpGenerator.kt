package me.scoretwo.utils.command.helper

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.SubCommand
import me.scoretwo.utils.plugin.GlobalPlugin
import net.md_5.bungee.api.chat.TextComponent

open class DefaultHelpGenerator(val plugin: GlobalPlugin): HelpGenerator {

    // 20 行聊天框长度
    override fun translateTexts(command: SubCommand, parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<TextComponent>> {
        val texts = mutableListOf<TextComponent>()

        texts.addAll(upperModule())

        command.subCommands.forEach { subCommand ->
            texts.add(TextComponent("§7/${parents.joinToString(" ") { it }} §f${subCommand.alias[0]} §8§l- §7${subCommand.description}"))
        }

        return mutableListOf(texts)
    }

    open fun upperModule() = mutableListOf(
        TextComponent(""),
        TextComponent(" §3${plugin.description.name} §7v${plugin.description.version} §8§l- §eCommand§6Nexus"),
        TextComponent("")
    )

    fun dividingLineModule(nowPage: Int, maxPage: Int): TextComponent {
        return TextComponent()
    }



}