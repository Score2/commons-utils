package me.scoretwo.utils.command

import com.andreapivetta.kolor.*
import net.md_5.bungee.api.chat.BaseComponent
import org.junit.Test

class CommandNexusTest {

    val commandNexus: CommandNexus = object : CommandNexus(arrayOf("test")) {

        override fun executed(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {

            when {
                parents[0] == "testOne" -> {
                    println(prefix + "parents[0] == testOne".green())
                    if (args[0] != "test2") {
                        println(prefix + "args[0] != test2".red())
                        println(prefix + "args[0] == ${args[0]}".red())
                    }
                    if (args[1] != "test3") {
                        println(prefix + "args[1] != test3".red())
                        println(prefix + "args[1] == ${args[1]}".red())
                    }
                }
            }

            return true
        }

    }

    @Test
    fun executeCommand() {

        commandNexus.execute(sender, mutableListOf("testOne"), mutableListOf("test2","test3"))

    }

}

val sender = object : GlobalSender {
    override val name: String = "TESTER"

    override fun sendMessage(message: String) = println(message)

    override fun sendMessage(messages: Array<String>) = println(messages)

    override fun hasPermission(name: String): Boolean {
        return true
    }

}

val prefix = "${Thread.currentThread().stackTrace[2].className}#${Thread.currentThread().stackTrace[2].methodName}".cyan() + " >>> ".lightGray()