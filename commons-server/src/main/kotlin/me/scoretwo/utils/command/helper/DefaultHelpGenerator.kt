package me.scoretwo.utils.command.helper

import me.scoretwo.utils.command.CommandNexus
import me.scoretwo.utils.command.SubCommand
import me.scoretwo.utils.plugin.GlobalPlugin
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text

open class DefaultHelpGenerator(val plugin: GlobalPlugin): HelpGenerator {

    // 去掉点击动作与预览文本用于尝试修复在 Mohist 上运行
    override fun translateTexts(command: SubCommand, parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<Array<TextComponent>>> {
        val texts = mutableListOf<Array<TextComponent>>()

        texts.addAll(upperModule)

        val displayParents = parents.joinToString(" ")

        command.subCommands.forEach { subCommand ->
            val displayAlia = subCommand.alias[0]
            val displayAlias =
                if (subCommand.alias.size < 2)
                    ""
                else
                    subCommand.alias.slice(1 until subCommand.alias.size)
                        .joinToString("/","§8[","§8]§7")

            val displayArgs =
                if (subCommand.subCommands.isEmpty() && subCommand.customCommands.isNotEmpty())
                    "§7<args...> "
                else if (subCommand.subCommands.isNotEmpty()) {
                    subCommand.subCommands.joinToString("/", "§7<", "§7> ", 5, "§8...") { it.alias[0] }
                } else
                    ""

            texts.add(arrayOf(TextComponent("§7/$displayParents §f$displayAlia$displayAlias §7$displayArgs§8§l- §7${subCommand.description}")/*.also {
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§7Click insert command: §f/$displayParents $displayAlia"))
                it.clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/$displayParents $displayAlia ")
            }*/))
        }

        command.customCommands.forEach {
            val displayAlia = it.key
            val displayArgs = it.value.first?.joinToString("/", "§7<", "§7> ", 5, "§8...") ?: ""
            texts.add(arrayOf(TextComponent("§7/$displayParents §f$displayAlia §7$displayArgs§8§l- §7${it.value.second}")/*.also {
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§7Click insert command: §f/$displayParents $displayAlia"))
                it.clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/$displayParents $displayAlia ")
            }*/))
        }

        if (upperModule.size == texts.size) {
            texts.add(arrayOf(TextComponent("No command data available.")/*.also {
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§7No command data available."))
            }*/))
        }


        return mutableListOf(texts)
    }

    open val upperModule = mutableListOf(
        arrayOf(TextComponent("")),
        arrayOf(
            TextComponent(" §3${plugin.description.name} §7v${plugin.description.version}")/*.also {
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(
                    """
                        §7Plugin: §a${plugin.description.name}
                        §7Description: §f${plugin.description.description}
                        §7Authors: §6${plugin.description.authors.joinToString(", ")}
                        §7Version: §b${plugin.description.version}
                    """.trimIndent()
                ))
            }*/,
            TextComponent(" §8§l- "),
            TextComponent("§eCommand§6Nexus")/*.also {
                it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(
                    """
                        
                        §c@Powered by Score2's §e§lcommons-utils
                        
                    """.trimIndent()
                ))
            }*/
        ),
        arrayOf(TextComponent(""))
    )

    open fun dividingLineModule(nowPage: Int, maxPage: Int): TextComponent {
        return TextComponent()
    }
}