package me.scoretwo.utils.sponge.configuration.yaml.patchs

import org.spongepowered.configurate.ScopedConfigurationNode
import org.spongepowered.configurate.loader.AbstractConfigurationLoader
import java.io.InputStream

fun <T : AbstractConfigurationLoader.Builder<T, L>, L : AbstractConfigurationLoader<*>> AbstractConfigurationLoader.Builder<T, L>.inputStream(inputStream: InputStream): T {
    return this.source { inputStream.bufferedReader() }
}

fun <T : AbstractConfigurationLoader.Builder<T, L>, L : AbstractConfigurationLoader<*>> AbstractConfigurationLoader.Builder<T, L>.string(string: String): T {
    return this.source { string.reader().buffered() }
}

fun <N : ScopedConfigurationNode<N>> ScopedConfigurationNode<N>.node(lowerCase: Boolean, vararg path: String): N {
    if (!lowerCase) {
        return this.node(*path)
    }
/*

    val finalPath = mutableListOf<String>()

    for (i in path.indices) {
        
    }

    for (i in 0 until childrenMap().keys.size) {
        val originalKey = childrenMap().keys.toTypedArray()[i].toString()

        if (path[i].equals(originalKey, ignoreCase = true)) {
            finalPath
        }


    }
*/
    TODO("忽略大小写读取节点")
}