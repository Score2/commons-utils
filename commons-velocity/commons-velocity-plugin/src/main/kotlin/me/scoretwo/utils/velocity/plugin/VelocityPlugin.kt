package me.scoretwo.utils.velocity.plugin

import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.plugin.PluginDescription
import me.scoretwo.utils.plugin.customPlugins
import me.scoretwo.utils.plugin.logging.GlobalLogger
import me.scoretwo.utils.plugin.logging.toGlobalLogger
import me.scoretwo.utils.server.GlobalServer
import me.scoretwo.utils.velocity.server.proxyServer
import me.scoretwo.utils.velocity.server.toGlobalServer
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.reflect.Field

fun PluginContainer.toGlobalPlugin(proxy: ProxyServer = proxyServer) = this.let { plugin ->
/*
    val pluginAnnotation: Plugin = plugin.instance.javaClass.getAnnotation(Plugin::class.java)
    for (field: Field in plugin.instance.javaClass.declaredFields) {
//        val inject = field.getAnnotation(Inject::class.java)
        field.isAccessible = true
        if (field.type != ProxyServer::javaClass || field[plugin] == null) continue
        proxyServer = field[plugin] as ProxyServer
        break
    }
*/
    proxyServer = proxy
    if (customPlugins.containsKey(plugin.description.name.orElseGet(null)))
        customPlugins[plugin.description.name.orElseGet(null)]!!
    else object : GlobalPlugin {
        override val server: GlobalServer = proxyServer.toGlobalServer()
        override val dataFolder: File = File("plugins", if (plugin.description.name.isPresent) plugin.description.name.get() else plugin.description.id)
        override val pluginClassLoader = plugin.javaClass.classLoader
        override val logger: GlobalLogger = LoggerFactory.getLogger(plugin.description.id).toGlobalLogger()
        override val description: PluginDescription = plugin.description.toPluginDescription()
    }.also { customPlugins[it.description.name] = it }
}

fun GlobalPlugin.toVelocityPlugin() = proxyServer.pluginManager.getPlugin(this.description.name).get()

fun com.velocitypowered.api.plugin.PluginDescription.toPluginDescription() = this.let { desc ->
    object : PluginDescription {
        override val name: String = if (desc.name.isPresent) desc.name.get() else desc.id
        override val version: String = if (desc.version.isPresent) desc.version.get() else "1.0.0"
        override val description: String = if (desc.description.isPresent) desc.description.get() else "No more description..."
        override val authors: Array<String> = desc.authors.toTypedArray()
    }
}