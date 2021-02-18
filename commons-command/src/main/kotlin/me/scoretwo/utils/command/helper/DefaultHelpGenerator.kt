package me.scoretwo.utils.command.helper

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.SubCommand
import me.scoretwo.utils.plugin.GlobalPlugin
import net.md_5.bungee.api.chat.TextComponent

open class DefaultHelpGenerator(val plugin: GlobalPlugin): HelpGenerator {

    // 20 行聊天框长度
    override fun translateTexts(command: SubCommand, parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<TextComponent>> {
        val texts = mutableListOf<TextComponent>()

        texts.addAll(upperModule)

        command.subCommands.forEach { subCommand ->
            val displayParents = parents.joinToString(" ")
            val displayAlia = subCommand.alias[0]
            val displayArgs = if (subCommand.moreArgs?.isEmpty() == true) "§7<args...> " else subCommand.moreArgs?.joinToString("/", "§7<", "§7> ", 8, "§8...") ?: ""
            texts.add(TextComponent("§7/$displayParents §f$displayAlia §7$displayArgs§8§l- §7${subCommand.description}"))
        }

        command.customCommands.forEach {
            val displayParents = parents.joinToString(" ")
            val displayAlia = it.key
            val displayArgs = it.value.first?.joinToString("/", "§7<", "§7> ", 8, "§8...") ?: ""
            texts.add(TextComponent("§7/$displayParents §f$displayAlia §7$displayArgs§8§l- §7${it.value.second}"))
        }

        if (upperModule.size == texts.size) {
            texts.add(TextComponent("No command data available."))
        }

        return mutableListOf(texts)
    }

    open val upperModule = mutableListOf(
        TextComponent(""),
        TextComponent(" §3${plugin.description.name} §7v${plugin.description.version} §8§l- §eCommand§6Nexus"),
        TextComponent("")
    )

    fun dividingLineModule(nowPage: Int, maxPage: Int): TextComponent {
        return TextComponent()
    }



}