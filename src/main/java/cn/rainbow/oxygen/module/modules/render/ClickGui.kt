package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.ModeValue
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "ClickGui", category = Category.Render)
class ClickGui: Module() {

    companion object {
        @JvmStatic
        val mode = ModeValue("Color", "Dark")
    }

    init {
        mode.addValue("Light")
        mode.addValue("Dark")
        this.keyCode = Keyboard.KEY_RSHIFT
    }

    override fun onEnable() {
        mc.displayGuiScreen(Oxygen.INSTANCE.briskGUI)
        this.enabled = false
    }

    override fun onDisable() {}
}