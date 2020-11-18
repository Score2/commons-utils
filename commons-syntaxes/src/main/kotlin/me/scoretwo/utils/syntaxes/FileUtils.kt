package me.scoretwo.utils.syntaxes

import java.io.*
import java.net.URL


object FileUtils {

    fun readString(file: File): String? {
        return try {
            val fileInputStream = FileInputStream(file)
            val raw = fileInputStream.readString()
            fileInputStream.close()
            raw
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun readStringList(file: File): ArrayList<String> {
        val result = ArrayList<String>()
        try {
            val br = BufferedReader(FileReader(file))
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

    fun save(file: File, url: URL) {
        save(file, UrlUtils.readString(url))
    }

    fun save(file: File, inputStream: InputStream) {
        try {
            val fileOutPutStream = FileOutputStream(file)
            fileOutPutStream.write(inputStream.readBytes())
            fileOutPutStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save(file: File, obj: Any) {
        try {
            val oos = ObjectOutputStream(FileOutputStream(file))
            oos.writeObject(obj)
            oos.flush()
            oos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun save(file: File, obj: List<String>) {
        if (file.exists())
            file.delete()
        try {
            val oos = PrintStream(file)
            val a = obj.size
            for (i in 0 until a)
                if (i < a - 1)
                    oos.println(obj[i])
                else
                    oos.print(obj[i])
            oos.flush()
            oos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <Type> load(file: File, t: Type): Type {
        if (!file.exists())
            return t
        var f: FileInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            f = FileInputStream(file)
            ois = ObjectInputStream(f)
            val obj = ois.readObject()
            ois.close()
            f.close()
            return if (obj != null) obj as Type else t
        } catch (e: ClassNotFoundException) {
            try {
                f?.close()
                ois?.close()
            } catch (e1: IOException) {
            }

        } catch (e: IOException) {
            try {
                f?.close()
                ois?.close()
            } catch (e1: IOException) {
            }
        }
        return t
    }
}

fun File.readString(): String? {
    return FileUtils.readString(this)
}

fun File.readStringList(): ArrayList<String> {
    return FileUtils.readStringList(this)
}

fun File.save(url: URL) {
    FileUtils.save(this, url)
}

fun File.save(inputStream: InputStream) {
    FileUtils.save(this, inputStream)
}

fun File.save(any: Any) {
    FileUtils.save(this, any)
}

fun File.save(list: List<String>) {
    FileUtils.save(this, list)
}

fun <Type> File.load(t: Type): Type {
    return FileUtils.load(this, t)
}