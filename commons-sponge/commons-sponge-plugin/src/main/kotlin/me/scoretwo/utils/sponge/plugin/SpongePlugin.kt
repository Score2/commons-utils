package me.scoretwo.utils.sponge.plugin

import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.plugin.PluginDescription
import me.scoretwo.utils.plugin.customPlugins
import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.plugin.logging.toGlobalLogger
import me.scoretwo.utils.server.GlobalServer
import me.scoretwo.utils.sponge.server.toGlobalServer
import org.spongepowered.api.Sponge
import org.spongepowered.api.plugin.PluginContainer
import java.io.File

fun PluginContainer.toGlobalPlugin() = this.let { plugin ->
    if (customPlugins.containsKey(plugin.name))
        customPlugins[plugin.name]!!
    else object : GlobalPlugin {
        override val server: GlobalServer = Sponge.getServer().toGlobalServer()
        override val dataFolder: File = File("config", plugin.name)
        override val pluginClassLoader = plugin.javaClass.classLoader
        override val logger: GlobalLogger = plugin.logger.toGlobalLogger()
        override val description: PluginDescription = this.let {
            object : PluginDescription {
                override val name: String = plugin.name
                override val version: String = plugin.version.let { if (it.isPresent) it.get() else "1.0.0" }
                override val description: String = plugin.description.let { if (it.isPresent) it.get() else "No more description..." }
                override val authors: Array<String> = plugin.authors.toTypedArray()
            }
        }
    }.also { customPlugins[it.description.name] = it }
}

fun GlobalPlugin.toSpongePlugin() = Sponge.getPluginManager().getPlugin(this.description.name.toLowerCase()).get()