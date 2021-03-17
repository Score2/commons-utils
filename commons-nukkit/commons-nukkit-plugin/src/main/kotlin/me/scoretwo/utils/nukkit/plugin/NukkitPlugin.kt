package me.scoretwo.utils.nukkit.plugin

import cn.nukkit.Server
import cn.nukkit.plugin.Plugin
import me.scoretwo.utils.nukkit.server.toGlobalServer
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.plugin.PluginDescription
import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.plugin.logging.toGlobalLogger

/**
 * @author 83669
 * @date 2021/3/17 20:38
 *
 * @project commons-utils
 */
fun Plugin.toGlobalPlugin() = this.let {
    object : GlobalPlugin {
        override val server = it.server.toGlobalServer()
        override val dataFolder = it.dataFolder
        override val pluginClassLoader = it.javaClass.classLoader
        override val logger: GlobalLogger = it.logger.let {
            object : GlobalLogger {
                override fun info(s: String) = it.info(s)
                override fun warn(s: String) = it.warning(s)
                override fun warn(s: String, t: Throwable) = it.warning(s, t)
                override fun error(s: String) = it.error(s)
                override fun error(s: String, t: Throwable) = it.error(s, t)
            }
        }
        override val description: PluginDescription
            get() = TODO("Not yet implemented")

    }
}

fun GlobalPlugin.toNukkitPlugin() = Server.getInstance().pluginManager.getPlugin(this.name)!!

fun cn.nukkit.plugin.PluginDescription.toPluginDescription() = this.let { desc ->
    object : PluginDescription {
        override val name = desc.name
        override val version = desc.version
        override val description = desc.description
        override val authors: Array<String> = desc.authors?.toTypedArray() ?: arrayOf()
    }
}