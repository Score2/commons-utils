package me.scoretwo.utils.command

import com.andreapivetta.kolor.red
import net.md_5.bungee.api.chat.BaseComponent
import org.junit.Test

class CommandNexusTest {

    val commandNexus: CommandNexus = object : CommandNexus(arrayOf("test"), object : CommandProcessor {
        override fun sendMessage(sender: Any, string: String) {
            println(string)
        }

        override fun sendMessage(sender: Any, component: BaseComponent) {
            println(component.toString())
        }

        override fun hasPermission(sender: Any, permission: String): Boolean {
            return true
        }

        override fun getName(sender: Any): String {
            return "Tester"
        }

    }) {

        override fun executed(sender: Any, parents: Array<String>, args: Array<String>): Boolean {

            when {
                parents[0] == "testOne" -> {
                    if (args[0] != "test2" || args[1] != "test3") {
                        println("[${javaClass.name}][executeCommand] testOne failed!".red())
                    }
                }
            }

            return true
        }

    }

    @Test
    fun executeCommand() {

        commandNexus.execute(this, arrayOf("testOne"), arrayOf("test2","test3"))

    }

}
