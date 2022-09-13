package cn.rainbow.oxygen.module

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.EventManager
import cn.rainbow.oxygen.file.files.ModuleConfig
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.Setting
import net.minecraft.client.Minecraft
import java.util.concurrent.CopyOnWriteArrayList

@Native
open class Module(val name: String, val category: Category) {

    constructor(name: String, category: Category, subModes: Array<SubMode>) : this(name, category) {
        if (subModes.isNotEmpty()) {
            val modeValue = ModeValue("Mode", "Normal")
            modeValue.addValue("Normal")
            for (sub in subModes) {
                if (!sub.name.equals("normal", ignoreCase = true)) {
                    modeValue.addValue(sub.name)
                }
            }
            this.subMode = true
            this.subModes = subModes
            this.settings.add(modeValue)
        }
    }

    @JvmField
    val mc: Minecraft = Minecraft.getMinecraft()

    var keyCode = 0

    var subMode = false
    var noSetEnable = false

    var subModes: Array<SubMode>? = null
    val settings = CopyOnWriteArrayList<Setting>()

    open fun onEnable() {}
    open fun onDisable() {}

    var displayName = ""

    var enabled = false
    set(value) {
        //NoSet Module
        if (noSetEnable) return
        field = value
        Oxygen.INSTANCE.filemanager.saveFile(ModuleConfig())
        if (value) {
            if (mc.theWorld != null) {
                if (mc.thePlayer != null) {
                    /*if (!Oxygen.INSTANCE.client.login) {
                        Oxygen.INSTANCE.exit()
                    }*/
                    mc.thePlayer.playSound("random.click", 1.0F, 1.0F)
                }
                onEnable()
            }
            EventManager.register(this)
            if (subMode) updateSubMode(value)
        } else {
            if (mc.theWorld != null) {
                if (mc.thePlayer != null) {
                    /*if (!Oxygen.INSTANCE.client.login) {
                        Oxygen.INSTANCE.exit()
                    }*/
                    mc.thePlayer.playSound("random.click", 1.0F, 0.8F)
                }
                if (subMode) updateSubMode(value)
                onDisable()
            }
            EventManager.unregister(this)
        }
    }

    private fun updateSubMode(toggle: Boolean) {
        val modeValue = Oxygen.INSTANCE.settingManager.getSetting(this, "Mode") as ModeValue
        if (!modeValue.isCurrentMode("Normal")) {
            val subMode = getSubMode(modeValue.currentValue) ?: return
            if (toggle) {
                subMode.onEnable()
                EventManager.register(subMode)
            } else {
                subMode.onDisable()
                EventManager.unregister(subMode)
            }
        }
    }

    private fun getSubMode(name: String): SubMode? {
        var subMode: SubMode? = null
        for (sub in subModes!!) {
            if (sub.name.equals(name, ignoreCase = true)) {
                subMode = sub
                break
            }
        }
        return subMode
    }
}

open class SubMode(val name: String) {

    open fun onEnable() {}
    open fun onDisable() {}
}