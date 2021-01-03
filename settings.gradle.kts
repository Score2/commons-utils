rootProject.name = "commons-utils"

private var prefix = "commons"

listOf(
    "syntaxes",
    "command"
).forEach {
    include(":${prefix}-${it}")
}

prefix = "commons-bukkit"
listOf(
    "command",
    "configuration"
).forEach {
    include(":${prefix}:${prefix}-${it}")
}

prefix = "commons-bungee"
listOf(
    "command"
).forEach {
    include(":${prefix}:${prefix}-${it}")
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