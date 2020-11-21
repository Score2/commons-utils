package me.scoretwo.utils.command.language

open class DefaultCommandLanguage: CommandLanguage {
    override val COMMAND_ONLY_CONSOLE: String = "&c该命令仅控制台才能执行"
    override val COMMAND_ONLY_PLAYER: String = "&c该命令仅玩家才能执行"
    override val COMMAND_NO_PERMISSION: String = "&c你没有权限执行该命令"
    override val COMMAND_UNKNOWN_USAGE: String = "&c用法 &e{usage_error} &c不正确, 你可能想用 &e{usage_guess}"
}