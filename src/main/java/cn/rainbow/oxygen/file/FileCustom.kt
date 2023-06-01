package cn.rainbow.oxygen.file

import java.io.*

abstract class FileCustom(val path: File = FileManager.path) {

    fun create(name: String) {
        val file = File(path, name)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadFile(name: String): File {
        return File(path, name)
    }

    fun loadFileForReader(name: String): InputStreamReader {
        return File(path, name).reader(Charsets.UTF_8)
    }

    abstract fun openFile()
    abstract fun saveFile()

}