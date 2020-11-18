package me.scoretwo.utils.syntaxes

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

object StreamUtils {

    fun getResource(filename: String): InputStream? {
        return try {
            val url: URL = javaClass.classLoader.getResource(filename) ?: return null
            val connection = url.openConnection()
            connection.useCaches = false
            connection.getInputStream()
        } catch (ex: IOException) {
            null
        }
    }

    fun readString(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }


    fun readStringList(inputStream: InputStream): ArrayList<String> {
        val result = ArrayList<String>()
        try {
            val br = BufferedReader(inputStream.bufferedReader())
            var s: String? = br.readLine()
            while (s != null) {
                result.add(s)
                s = br.readLine()
            }
            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}
fun InputStream.readString(): String {
    return StreamUtils.readString(this)
}