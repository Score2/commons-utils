package me.scoretwo.utils.bukkit.plugin

import me.scoretwo.utils.bukkit.server.toGlobalServer
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.plugin.PluginDescription
import me.scoretwo.utils.plugin.customPlugins
import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.plugin.logging.toGlobalLogger
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.GlobalServer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

fun Plugin.toGlobalPlugin() = this.let { plugin ->
    if (customPlugins.containsKey(plugin.name))
        customPlugins[plugin.name]!!
    else object : GlobalPlugin {
        override val server: GlobalServer = plugin.server.toGlobalServer()
        override val dataFolder: File = plugin.dataFolder
        override val pluginClassLoader = plugin.javaClass.classLoader
        override val logger: GlobalLogger = plugin.logger.toGlobalLogger()
        override val description: PluginDescription = plugin.description.toPluginDescription()
    }.also { customPlugins[it.description.name] = it }
}

fun GlobalPlugin.toBukkitPlugin() = Bukkit.getPluginManager().getPlugin(this.description.name)!!

fun PluginDescriptionFile.toPluginDescription() = this.let { desc ->
    object : PluginDescription {
        override val name: String = desc.name
        override val version: String = desc.version
        override val description: String = desc.description.let { it ?: "No more description..." }
        override val authors: Array<String> = desc.authors.toTypedArray()
    }
}