package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.ModeValue

@ModuleInfo(name = "Animation", category = Category.Render, noSetEnable = true)
class Animation: Module() {
    val mode = ModeValue("Mode", "Normal", arrayOf("Normal", "Exhi", "Sigma", "1.7"))
}