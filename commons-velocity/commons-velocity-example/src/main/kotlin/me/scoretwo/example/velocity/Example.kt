package me.scoretwo.example.velocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import me.scoretwo.utils.plugin.GlobalPlugin
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import me.scoretwo.utils.velocity.plugin.toGlobalPlugin


@Plugin(
    id = "example",
    name = "Example",
    version = "1.0.0",
    authors = ["Score2"],
    description = "A example plugin"
)
class Example {

    @Inject
    lateinit var proxy: ProxyServer

    @Subscribe(order = PostOrder.NORMAL)
    fun onEnable(e: ProxyInitializeEvent) {
        val plugin = proxy.pluginManager.fromInstance(this).get()
        val globalPlugin = plugin.toGlobalPlugin(proxy)
    }

    @Subscribe(order = PostOrder.NORMAL)
    fun onDisable(e: ProxyShutdownEvent?) {

    }
}