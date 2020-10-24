package me.scoretwo.utils.configuration.patchs

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

fun File.loadConfiguration(): YamlConfiguration {
    return YamlConfiguration.loadConfiguration(this)
}

fun File.saveConfiguration(yamlConfiguration: YamlConfiguration) {
    yamlConfiguration.save(this)
}

fun ConfigurationSection.getLowerCaseNode(lowerCasePath: String): String {
    val isMultiPath = lowerCasePath.contains(".")
    val firstNode = if (!isMultiPath) lowerCasePath else lowerCasePath.substring(0, lowerCasePath.indexOf(".") - 1)

    for (node in getKeys(false)) {
        if (firstNode == node.toLowerCase()) {
            return if (!isMultiPath) {
                node
            } else {
                val afterNode = lowerCasePath.substring(lowerCasePath.indexOf("."))

                "${node}.${getLowerCaseNode(afterNode)}"
            }
        }
    }

    return ""
}

fun ConfigurationSection.getConfigurationSectionList(path: String): List<ConfigurationSection>? {
    val list = getList(path) ?: return null
    val result = mutableListOf<ConfigurationSection>()

    for (raw in list) {
        val yamlConfiguration = YamlConfiguration()
        when (raw) {
            is MemorySection -> result.add(yamlConfiguration)
            is List<*> -> raw.forEach { any ->
                val args = any.toString().split(Regex(":"), 2)
                if (args.size == 2) yamlConfiguration.set(args[0], args[1])
                result.add(yamlConfiguration)
            }
            is Map<*, *> -> {
                raw.entries.forEach { entry -> yamlConfiguration.set(entry.key.toString(), entry.value) }
                result.add(yamlConfiguration)
            }
        }
    }

    return result
}