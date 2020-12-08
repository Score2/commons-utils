package me.scoretwo.utils.bukkit.command.patchs

import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap

val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap