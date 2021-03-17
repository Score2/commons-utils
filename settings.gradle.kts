rootProject.name = "commons-utils"

/**
 * @author Score2
 * @version 1.2
 */
class MergeBuilder {
    val nodes: MutableList<String>

    constructor(vararg nodes: String): this(nodes.toMutableList())
    constructor(nodes: MutableList<String>) {
        this.nodes = nodes
    }
    constructor(merge: MergeBuilder, vararg addNodes: String): this(merge.nodes, addNodes.toMutableList())
    constructor(merge: MergeBuilder, addNodes: MutableList<String>): this(merge.nodes, addNodes)
    constructor(nodes: MutableList<String>, vararg addNodes: String): this(nodes, addNodes.toMutableList())
    constructor(nodes: MutableList<String>, addNodes: MutableList<String>) {
        this.nodes = nodes.also { it.addAll(addNodes) }
    }

    fun add(vararg addNodes: String) = add(addNodes.toMutableList())
    fun add(addNodes: MutableList<String> = mutableListOf()) {
        val editedList = mutableListOf<String>().also { it.addAll(nodes); it.addAll(addNodes) }
        val finallyNode = java.lang.StringBuilder()
        for (i in 0 until editedList.size) {
            finallyNode.append(":${editedList[i]}")
        }
        include(finallyNode.toString())
    }

    fun merge(vararg addNodes: String) = merge(addNodes.toMutableList())
    fun merge(addNodes: MutableList<String> = mutableListOf()): MergeBuilder {
        val editedList = mutableListOf<String>().also { it.addAll(nodes); it.addAll(addNodes) }
        val finallyNode = java.lang.StringBuilder()
        for (i in 0 until editedList.size) {
            val currentNode = java.lang.StringBuilder()
            for (j in 0..i) {
                currentNode.append(if (currentNode.isEmpty()) editedList[j] else "-${editedList[j]}")
                // println("   j $currentNode")
            }

            if (currentNode.isNotEmpty())
                finallyNode.append(":$currentNode")
            // println("i $finallyNode")
        }
        include(finallyNode.toString())
        return this
    }

    fun newBuilder(vararg addNodes: String): MergeBuilder = newBuilder(addNodes.toMutableList())
    fun newBuilder(addNodes: MutableList<String>): MergeBuilder {
        return MergeBuilder(nodes, addNodes)
    }
}

include("commons-syntaxes")
include("commons-server")
include("commons-language")

MergeBuilder("commons-sponge").also {
    it.merge("plugin")
    it.merge("example")
    it.newBuilder("configuration").also {
        it.merge("yaml")
        it.merge("xaml")
    }
}

MergeBuilder("commons-bukkit").also {
    it.merge("plugin")
    it.merge("configuration")
}

MergeBuilder("commons-bungee").also {
    it.merge("plugin")
}

MergeBuilder("commons-velocity").also {
    it.merge("plugin")
    it.merge("example")
}

MergeBuilder("commons-nukkit").also {
    it.merge("plugin")
}
