rootProject.name = "commons-utils"

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
}

include("commons-syntaxes")
include("commons-command")

MergeBuilder("commons-sponge", "configuration").let {
    it.merge("yaml")
    it.merge("xaml")
}

MergeBuilder("commons-bukkit").let {
    it.merge("command")
    it.merge("configuration")
}

MergeBuilder("commons-bungee").let {
    it.merge("command")
}

/*
include("commons-syntaxes")
include("commons-command")
include("commons-bukkit")
include("commons-bukkit:commons-bukkit-configuration")
findProject(":commons-bukkit:commons-bukkit-configuration")?.name = "commons-bukkit-configuration"
include("commons-bukkit:commons-bukkit-configuration:commons-bukkit-configuration-yaml")
findProject(":commons-bukkit:commons-bukkit-configuration:commons-bukkit-configuration-yaml")?.name = "commons-bukkit-configuration-yaml"
include("commons-bukkit:commons-bukkit-command")
findProject(":commons-bukkit:commons-bukkit-command")?.name = "commons-bukkit-command"

include("commons-bungee")
include("commons-bungee:commons-bungee-command")
findProject(":commons-bungee:commons-bungee-command")?.name = "commons-bungee-command"

include("commons-sponge")
include("commons-sponge:commons-sponge-configuration")
findProject(":commons-sponge:commons-sponge-configuration")?.name = "commons-sponge-configuration"
include("commons-sponge:commons-sponge-configuration:commons-sponge-configuration-xaml")
findProject(":commons-sponge:commons-sponge-configuration:commons-sponge-configuration-xaml")?.name = "commons-sponge-configuration-xaml"
include("commons-sponge:commons-sponge-configuration:commons-sponge-configuration-yaml")
findProject(":commons-sponge:commons-sponge-configuration:commons-sponge-configuration-yaml")?.name = "commons-sponge-configuration-yaml"


include("commons-integration")