package cn.rainbow.oxygen.file

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.file.files.ModuleConfig
import java.io.File

class FileManager {

    var files: ArrayList<FileConfig> = ArrayList()
    val fileCustoms: ArrayList<FileCustom> = ArrayList()

    companion object {
        @JvmStatic
        val path = File(System.getProperty("user.dir") + File.separator + Oxygen.name + "-1.8.9")
    }

    init {
        pathCheck(path)
        addFile(ModuleConfig())
    }

    private fun pathCheck(file: File) {
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun addFile(fileConfig: Any) {
        if (fileConfig is FileConfig){
            files.add(fileConfig)
        } else if (fileConfig is FileCustom){
            fileCustoms.add(fileConfig)
        }
    }

    fun loadAllFile() {
        this.files.forEach(this::loadFile)
        this.fileCustoms.forEach(this::loadFile)
    }

    fun loadFile(config: Any) {
        if (config is FileConfig){
            if (!config.hasConfig()) {
                saveFile(config)
            } else {
                config.openFile()
            }
        } else if (config is FileCustom){
            config.openFile()
        }
    }

    fun saveAllFile() {
        this.files.forEach(this::saveFile)
        this.fileCustoms.forEach(this::saveFile)
    }

    fun saveFile(config: Any) {
        if (config is FileConfig) {
            config.saveFile()
        } else if (config is FileCustom) {
            config.saveFile()
        }
    }
}