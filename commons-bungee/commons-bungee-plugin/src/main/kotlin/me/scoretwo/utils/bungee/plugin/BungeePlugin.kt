package me.scoretwo.utils.bungee.plugin

import me.scoretwo.utils.bungee.server.toGlobalServer
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.plugin.PluginDescription
import me.scoretwo.utils.plugin.customPlugins
import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.plugin.logging.toGlobalLogger
import me.scoretwo.utils.server.GlobalServer
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.net.ProxySelector

fun Plugin.toGlobalPlugin() = this.let { plugin ->
    if (customPlugins.containsKey(plugin.description.name))
        customPlugins[plugin.description.name]!!
    else object : GlobalPlugin {
        override val server: GlobalServer = plugin.proxy.toGlobalServer()
        override val dataFolder: File = plugin.dataFolder
        override val pluginClassLoader: ClassLoader = plugin.javaClass.classLoader
        override val logger: GlobalLogger = plugin.logger.toGlobalLogger()
        override val description: PluginDescription = plugin.description.toPluginDescription()

    }
}

fun GlobalPlugin.toBungeePlugin() = ProxyServer.getInstance().pluginManager.getPlugin(this.description.name)!!

fun net.md_5.bungee.api.plugin.PluginDescription.toPluginDescription() = this.let { desc ->
    object : PluginDescription {
        override val name: String = desc.name
        override val version: String = desc.version
        override val description: String = desc.description.let { it ?: "No more description..." }
        override val authors: Array<String> = arrayOf(desc.author)
    }
}