package cn.rainbow.oxygen.file

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.*
import java.nio.charset.StandardCharsets


abstract class FileConfig(val name: String) {

    var file: File
    val path = FileManager.path

    init {
        this.file = File(path, "$name.json")
        if (!path.exists()) {
            try {
                path.mkdir()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    abstract fun openFile()
    abstract fun saveFile()

    fun readJsonFile(): JsonElement? {
        val jsonElement: JsonElement
        try {
            val br = BufferedReader(InputStreamReader(FileInputStream(file.absolutePath), StandardCharsets.UTF_8))
            jsonElement = JsonParser().parse(br)
            return jsonElement
        } catch (e: FileNotFoundException) {
            println("Config file " + this.name + " not exist!")
        }
        return null
    }

    fun write(value: Any) {
        writeToFile(GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(value))
    }

    fun writeToFile(data: String) {
        try {
            val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(file, true), StandardCharsets.UTF_8))
            bw.write(data)
            bw.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun clear() {
        try {
            val bw = BufferedWriter(FileWriter(file))
            bw.write("")
            bw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hasConfig(): Boolean {
        return file.exists()
    }

}