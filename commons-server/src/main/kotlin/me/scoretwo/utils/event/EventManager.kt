package me.scoretwo.utils.event

import me.scoretwo.utils.plugin.GlobalPlugin
import java.util.function.Consumer

/**
 * @author 83669
 * @date 2021/3/17 12:42
 *
 * @project commons-utils
 */
abstract class EventManager {

    // plugin, <listenerToken, listener>
    val listeners = mutableMapOf<GlobalPlugin, MutableMap<String, Any?>>()

    // events,
    abstract fun generateInstance(plugin: GlobalPlugin, token: String, handlers: MutableMap<String, MutableList<Consumer<Any>>>)

    open fun registerListener(plugin: GlobalPlugin, token: String, listener: Any?) {
        listeners[plugin] = mutableMapOf(token to listener)
    }

    open fun unregisterListener(plugin: GlobalPlugin, listener: Any?) {
        listeners.forEach {
            it.value.forEach {
                if (it.value?.javaClass?.name == listener?.javaClass?.name) {
                    listeners[plugin]?.remove(it.key)
                    return
                }
            }
        }
    }

    fun unregisterListener(plugin: GlobalPlugin, token: String) {
        listeners.forEach {
            it.value.forEach {
                if (it.key == token) {
                    unregisterListener(plugin, it.value)
                    return
                }
            }
        }
    }
}