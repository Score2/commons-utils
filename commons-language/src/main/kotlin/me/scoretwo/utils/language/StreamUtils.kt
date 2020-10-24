package me.scoretwo.utils.language

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream

object StreamUtils {

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