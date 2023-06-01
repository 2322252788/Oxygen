package cn.rainbow.oxygen.module

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventManager
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.file.files.ModuleConfig
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.Setting
import net.minecraft.client.Minecraft
import java.util.concurrent.CopyOnWriteArrayList

@Native
open class Module() {

    protected fun putSubModes(subModeList: Array<SubMode>) {
        if (subModeList.isNotEmpty()) {
            val modeValue = ModeValue("Mode", "Normal")
            subModeList.forEach { s ->
                try {
                    modeValue.addValue(s.name)
                } catch (e: Exception) {
                    throw e
                }
            }
            this.subMode = true
            this.subModes = subModeList
            this.settings.add(modeValue)
            this.subModeValue = modeValue
        }
    }

    @JvmField
    val mc: Minecraft = Minecraft.getMinecraft()

    val name: String
    val category: Category
    var keyCode = 0
    val noSetEnable: Boolean

    private var subMode = false
    private var subModeValue: ModeValue? = null
    var subModes: Array<SubMode>? = null
    private var lastSubMode: SubMode? = null

    val settings = CopyOnWriteArrayList<Setting>()

    open fun onEnable() {}
    open fun onDisable() {}

    var displayName = ""

    var enabled = false
    set(value) {
        //NoSet Module
        if (noSetEnable || !Oxygen.INSTANCE.isLoad) return
        field = value
        if (value) {
            Oxygen.INSTANCE.filemanager.saveFile(ModuleConfig())
            if (mc.theWorld != null && mc.thePlayer != null) {
                /*if (!Oxygen.INSTANCE.client.login) {
                        Oxygen.INSTANCE.exit()
                    }*/
                mc.thePlayer.playSound("random.click", 1.0F, 1.0F)
            }
            onEnable()
            EventManager.register(this)
            if (subMode) toggleSubMode(value)
        } else {
            if (mc.theWorld != null && mc.thePlayer != null) {
                /*if (!Oxygen.INSTANCE.client.login) {
                        Oxygen.INSTANCE.exit()
                    }*/
                mc.thePlayer.playSound("random.click", 1.0F, 0.8F)
            }
                if (subMode) toggleSubMode(value)
                EventManager.unregister(this)
                onDisable()
        }
    }

    init {
        val moduleInfo = javaClass.getAnnotation(ModuleInfo::class.java)
        name = moduleInfo.name
        category = moduleInfo.category
        enabled = moduleInfo.toggle
        noSetEnable = moduleInfo.noSetEnable
    }

    @EventTarget(events = [UpdateEvent::class])
    private fun updateSubMode(event: Event) {
        if (event is UpdateEvent) {
            if (this.subModeValue != null && this.lastSubMode != null) {
                if (!this.lastSubMode!!.name.equals(this.subModeValue!!.currentValue, ignoreCase = true)) {

                    EventManager.unregister(lastSubMode)
                    this.lastSubMode!!.onDisable()

                    val subMode = getSubMode(this.subModeValue!!.currentValue) ?: return
                    subMode.onEnable()
                    EventManager.register(subMode)

                    lastSubMode = subMode
                }
            }
        }
    }

    private fun toggleSubMode(toggle: Boolean) {
        if (this.subModeValue == null) return
        val subMode = getSubMode(this.subModeValue!!.currentValue) ?: return
        if (toggle) {
            subMode.onEnable()
            EventManager.register(subMode)
            this.lastSubMode = subMode
        } else {
            EventManager.unregister(subMode)
            subMode.onDisable()
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

    @JvmField
    protected val mc = Minecraft.getMinecraft()

    open fun onEnable() {}
    open fun onDisable() {}
}