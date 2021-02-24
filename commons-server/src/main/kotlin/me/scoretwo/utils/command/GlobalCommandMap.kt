package me.scoretwo.utils.command

import me.scoretwo.utils.plugin.GlobalPlugin

/**
 * @author Score2
 * @date 2021/2/24 11:23
 *
 * @project commons-utils
 */
abstract class GlobalCommandMap {

    val commandNexuses = mutableListOf<CommandNexus>()

    open fun register(nexus: CommandNexus) {
        commandNexuses.add(nexus)
    }

    open fun unregister(nexus: CommandNexus) {
        commandNexuses.remove(nexus)
    }

    fun getNexusesByPlugin(plugin: GlobalPlugin) = mutableListOf<CommandNexus>().also { list -> commandNexuses.forEach { if (it.plugin == plugin) list.add(it) } }

    fun getNexusesByAlia(alia: String): CommandNexus? {
        commandNexuses.forEach { if (it.alias.contains(alia)) return it }
        return null
    }

}