package cn.rainbow.oxygen.module.setting

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.module.Module
import java.lang.IllegalAccessException
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList

class SettingManager {

    init {
        registerSettings()
    }

    private fun registerSettings() {
        for (mod in Oxygen.INSTANCE.moduleManager.modules) {
            for (field in mod.javaClass.declaredFields) {
                field.isAccessible = true
                try {
                    if (field.type == BooleanValue::class.java) {
                        mod.settings.add(field[mod] as BooleanValue)
                    } else if (field.type == NumberValue::class.java) {
                        mod.settings.add(field[mod] as NumberValue)
                    } else if (field.type == ModeValue::class.java) {
                        mod.settings.add(field[mod] as ModeValue)
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun getSettings(): ArrayList<Setting> {
        val settings = ArrayList<Setting>()
        for (mod in Oxygen.INSTANCE.moduleManager.modules) {
            settings.addAll(mod.settings)
        }
        return settings
    }

    fun getSettingsByMod(mod: Module): CopyOnWriteArrayList<Setting> {
        return mod.settings
    }

    fun getSetting(mod: Module, name: String): Setting? {
        for (setting in mod.settings) {
            if (setting.name.equals(name, ignoreCase = true)) {
                return setting
            }
        }
        return null
    }

    fun getModBySettings(setting: Setting): Module? {
        for (mod in Oxygen.INSTANCE.moduleManager.modules) {
            if (mod.settings.contains(setting)) {
                return mod
            }
        }
        return null
    }
}