package me.scoretwo.utils.command.helper

import net.md_5.bungee.api.chat.TextComponent

open class DefaultHelpGenerator(pluginName: String, pluginVersion: String): HelpGenerator {

    override val description: HelpGenerator.Companion.Description =
        HelpGenerator.Companion.Description(pluginName, pluginVersion)

    // 20 行聊天框长度
    override fun translateTexts(
        parents: MutableList<String>,
        args: MutableList<String>
    ): MutableList<MutableList<TextComponent>> {
        val texts = mutableListOf<TextComponent>()

        texts.add(TextComponent(""))
        texts.add(TextComponent(" &3${description.name} &7v${description.version} &8&l- &eCommand&6Nexus"))
        return mutableListOf(texts)
    }

    open fun upperModule() = mutableListOf(
        TextComponent(""),
        TextComponent(" &3${description.name} &7v${description.version} &8&l- &eCommand&6Nexus"),
        TextComponent("")
    )

    fun dividingLineModule(nowPage: Int, maxPage: Int): TextComponent {
        return TextComponent()
    }


}