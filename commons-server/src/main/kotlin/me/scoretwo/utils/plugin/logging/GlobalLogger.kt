package me.scoretwo.utils.plugin.logging

interface GlobalLogger {

    fun info(s: String)

    fun warn(s: String)
    fun warn(s: String, t: Throwable)

    fun error(s: String)
    fun error(s: String, t: Throwable)

}

fun java.util.logging.Logger.toGlobalLogger() = this.let {
    object : GlobalLogger {
        override fun info(s: String) = it.info(s)
        override fun warn(s: String) = it.warning(s)
        override fun warn(s: String, t: Throwable) = it.log(java.util.logging.Level.WARNING, s, t)
        override fun error(s: String) = it.log(java.util.logging.Level.SEVERE, s)
        override fun error(s: String, t: Throwable) = it.log(java.util.logging.Level.SEVERE, s, t)
    }
}

fun org.slf4j.Logger.toGlobalLogger() = this.let {
    object : GlobalLogger {
        override fun info(msg: String) = it.info(msg)
        override fun warn(msg: String) = it.warn(msg)
        override fun warn(msg: String, t: Throwable) = it.warn(msg, t)
        override fun error(msg: String) = it.error(msg)
        override fun error(msg: String, t: Throwable) = it.error(msg, t)
    }
}