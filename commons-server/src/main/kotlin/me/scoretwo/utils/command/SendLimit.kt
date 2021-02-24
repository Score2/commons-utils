package me.scoretwo.utils.command

enum class SendLimit {
    PLAYER,
    CONSOLE,
    ALL;

    var permission = true

    fun permission(need: Boolean): SendLimit = this.also {
        it.permission = need
    }
}