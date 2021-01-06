package me.scoretwo.utils.command

import com.andreapivetta.kolor.*
import net.md_5.bungee.api.chat.BaseComponent
import org.junit.Test

class CommandNexusTest {

    val commandNexus: CommandNexus = object : CommandNexus(arrayOf("test")) {

        init {
            subCommands.add(object : SubCommand(arrayOf("test4")) {

                override fun executed(
                    sender: GlobalSender,
                    parents: MutableList<String>,
                    args: MutableList<String>
                ): Boolean {
                    println(prefix + "test4 succeed execute!".green())
                    return true
                }

            })
        }

        override fun executed(sender: GlobalSender, parents: MutableList<String>, args: MutableList<String>): Boolean {

            when {
                parents[0] == "testOne" -> {
                    println(prefix + "parents[0] == testOne".green())
                    if (args[0] != "test2") {
                        println(prefix + "args[0] != test2".red())
                        println(prefix + "args[0] == ${args[0]}".red())
                    } else {
                        println(prefix + "test2 succeed execute!".green())
                    }
                    if (args[1] != "test3") {
                        println(prefix + "args[1] != test3".red())
                        println(prefix + "args[1] == ${args[1]}".red())
                    } else {
                        println(prefix + "test3 succeed execute!".green())
                    }
                }
            }

            return true
        }

    }

    @Test
    fun executeCommand() {

        commandNexus.execute(sender, mutableListOf("testOne"), mutableListOf("test2","test3"))

        commandNexus.execute(sender, mutableListOf("testTwo"), mutableListOf("test4"))

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