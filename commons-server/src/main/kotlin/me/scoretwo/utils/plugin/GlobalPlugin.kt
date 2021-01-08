package me.scoretwo.utils.plugin

import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.GlobalServer
import java.io.File

interface GlobalPlugin {
    val server: GlobalServer
    val dataFolder: File
    val pluginClassLoader: ClassLoader
    val logger: GlobalLogger
    val description: PluginDescription
}
// pluginName, pluginInstance
val customPlugins = mutableMapOf<String, GlobalPlugin>()