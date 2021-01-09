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
        override fun info(s: String) = it.info(s)
        override fun warn(s: String) = it.warn(s)
        override fun warn(s: String, t: Throwable) = it.warn(s, t)
        override fun error(s: String) = it.error(s)
        override fun error(s: String, t: Throwable) = it.error(s, t)
    }
}