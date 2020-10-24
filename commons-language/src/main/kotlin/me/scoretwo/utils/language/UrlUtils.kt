package me.scoretwo.utils.language

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets

object UrlUtils {

    fun readString(url: URL): String {
        return try {
            val inputStream = url.openStream()
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            val stringBuilder = StringBuilder()
            var i: Int
            while (bufferedReader.read().also { i = it } != -1) {
                stringBuilder.append(i.toChar())
            }
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

}

fun URL.readString(): String {
    return UrlUtils.readString(this)
}