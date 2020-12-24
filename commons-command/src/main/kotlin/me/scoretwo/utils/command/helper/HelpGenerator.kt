package me.scoretwo.utils.command.helper

import net.md_5.bungee.api.chat.TextComponent

interface HelpGenerator {

    val description: Description

    fun translateTexts(parents: MutableList<String>, args: MutableList<String>): MutableList<MutableList<TextComponent>>

    companion object {
        class Description(val name: String, val version: String)
    }

}