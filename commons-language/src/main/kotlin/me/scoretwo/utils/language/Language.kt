package me.scoretwo.utils.language

abstract class Language {

    private val laguages = mutableMapOf<String, Any?>()

    fun <T> get(section: String, clazz: Class<T>): T? {
        val element = this.laguages[section.toLowerCase()]
        if (element!= null && element.javaClass.typeName == clazz.typeName) {
            return element as T
        }
        return null
    }

    fun set(section: String, value: Any?) {
        this.laguages[section.toLowerCase()] = value
    }

}