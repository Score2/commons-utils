package me.scoretwo.utils.command.language

interface CommandLanguage {

    val COMMAND_ONLY_CONSOLE: String
    val COMMAND_ONLY_PLAYER: String

    // {permission_name} - 需要的权限名称
    val COMMAND_NO_PERMISSION: String

    // {usage_guess} - 从错误用法中猜测可能需要的选项
    // {usage_error} - 错误的用法(当前输入的用法)
    val COMMAND_UNKNOWN_USAGE: String

}