package me.scoretwo.utils.command

interface HelpGenerator {

    fun translateTexts(parents: Array<String>, label: String, args: Array<String>): MutableList<MutableList<String>>

}