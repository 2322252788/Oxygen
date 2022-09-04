package cn.rainbow.oxygen.module

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.EventManager
import cn.rainbow.oxygen.file.files.ModuleConfig
import cn.rainbow.oxygen.module.setting.Setting
import net.minecraft.client.Minecraft
import java.util.concurrent.CopyOnWriteArrayList

@Native
open class Module(val name: String, val category: Category) {

    @JvmField
    val mc: Minecraft = Minecraft.getMinecraft()

    var keyCode = 0

    var noSetEnable = false

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
        } else {
            if (mc.theWorld != null) {
                if (mc.thePlayer != null) {
                    /*if (!Oxygen.INSTANCE.client.login) {
                        Oxygen.INSTANCE.exit()
                    }*/
                    mc.thePlayer.playSound("random.click", 1.0F, 0.8F)
                }
                onDisable()
            }
            EventManager.unregister(this)
        }
    }
}