package cn.rainbow.oxygen.file.files

import cn.rainbow.oxygen.file.FileConfig
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import cn.rainbow.oxygen.Oxygen
import com.google.gson.JsonArray
import java.util.function.Consumer

class ModuleConfig : FileConfig("Module") {
    override fun openFile() {
        val jsonElement = readJsonFile()
        if (jsonElement == null || !jsonElement.isJsonObject) {
            saveFile()
            return
        }
        val jsonObject = jsonElement.asJsonObject
        for (module in Oxygen.INSTANCE.moduleManager.modules) {
            if (jsonObject.has(module.name)) {
                val jsonValue = jsonObject[module.name].asJsonObject
                if (jsonValue.has("Toggled")) {
                    module.enabled = jsonValue["Toggled"].asBoolean
                }
                if (jsonValue.has("Key")) {
                    module.keyCode = jsonValue["Key"].asInt
                }
                if (jsonValue.has("Settings")) {
                    val jsonArray = jsonValue["Settings"].asJsonArray
                    module.settings.forEach { value ->
                        jsonArray.forEach { settings: JsonElement ->
                            if (value.getBooleanValue() != null) { // Boolean
                                if (settings.asJsonObject.has(value.name)) {
                                    value.getBooleanValue()!!.currentValue =
                                        settings.asJsonObject[value.name].asBoolean
                                }
                            } else if (value.getNumberValue() != null) { // Double
                                if (settings.asJsonObject.has(value.name)) {
                                    value.getNumberValue()!!.currentValue =
                                        settings.asJsonObject[value.name].asDouble
                                }
                            } else if (value.getModeValue() != null) { // Mode
                                if (settings.asJsonObject.has(value.name)) {
                                    value.getModeValue()!!.currentValue = settings.asJsonObject[value.name].asString
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun saveFile() {
        this.clear()
        val jsonObject = JsonObject()
        for (module in Oxygen.INSTANCE.moduleManager.modules) {
            val jsonModule = JsonObject()
            jsonModule.addProperty("Toggled", module.enabled)
            jsonModule.addProperty("Key", module.keyCode.toString())
            if (module.settings.size != 0) {
                val jsonSetting = JsonObject()
                val jsonArray = JsonArray()
                module.settings.forEach(Consumer { value ->
                    if (value.getBooleanValue() != null) { // Boolean
                        jsonSetting.addProperty(value.name, value.getBooleanValue()!!.currentValue)
                    } else if (value.getNumberValue() != null) { // Double
                        jsonSetting.addProperty(value.name, value.getNumberValue()!!.currentValue)
                    } else if (value.getModeValue() != null) { // Mode
                        jsonSetting.addProperty(value.name, value.getModeValue()!!.currentValue)
                    }
                })
                jsonArray.add(jsonSetting)
                jsonModule.add("Settings", jsonArray)
            }
            jsonObject.add(module.name, jsonModule)
        }
        write(jsonObject)
    }
}