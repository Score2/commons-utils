package me.scoretwo.utils.command.helper

import me.scoretwo.utils.command.CommandNexus
import net.md_5.bungee.api.chat.TextComponent

open class DefaultHelpGenerator(override val nexus: CommandNexus): HelpGenerator {

    // 20 行聊天框长度
    override fun translateTexts(parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<TextComponent>> {
        val texts = mutableListOf<TextComponent>()

        texts.add(TextComponent(""))
        texts.add(TextComponent(" &3${nexus.plugin.description.name} &7v${nexus.plugin.description.version} &8&l- &eCommand&6Nexus"))
        return mutableListOf(texts)
    }

    open fun upperModule() = mutableListOf(
        TextComponent(""),
        TextComponent(" &3${nexus.plugin.description.name} &7v${nexus.plugin.description.version} &8&l- &eCommand&6Nexus"),
        TextComponent("")
    )

    fun dividingLineModule(nowPage: Int, maxPage: Int): TextComponent {
        return TextComponent()
    }


}