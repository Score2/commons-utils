package me.scoretwo.utils.bukkit.configuration.yaml.patchs

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

val ConfigurationSection.keys: List<String> get() = getKeys(false).toList()

@Deprecated("There is a better alternative", ReplaceWith("ignoreCase(path)"))
fun ConfigurationSection.getLowerCaseNode(path: String) = ignoreCase(path)

@Deprecated("There is a better alternative", ReplaceWith("ignoreCase(path)"))
fun ConfigurationSection.getUpperCaseNode(path: String) = ignoreCase(path)

fun ConfigurationSection.ignoreCase(path: String): String {
    for (node in getKeys(true)) {
        if (node.equals(path, ignoreCase = true)) {
            return node
        }
    }
    return path
}

fun ConfigurationSection.getConfigurationSectionList(path: String, def: List<ConfigurationSection>? = null): List<ConfigurationSection>? = mutableListOf<ConfigurationSection>().also {
    val list = getList(path) ?: return def

    for (raw in list) {
        val yamlConfiguration = YamlConfiguration()
        when (raw) {
            is MemorySection -> it.add(yamlConfiguration)
            is List<*> -> raw.forEach { any ->
                val args = any.toString().split(Regex(":"), 2)
                if (args.size == 2) yamlConfiguration.set(args[0], args[1])
                it.add(yamlConfiguration)
            }
            is Map<*, *> -> {
                raw.entries.forEach { entry -> yamlConfiguration.set(entry.key.toString(), entry.value) }
                it.add(yamlConfiguration)
            }
        }
    }
}