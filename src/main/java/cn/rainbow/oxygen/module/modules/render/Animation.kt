package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.ModeValue

class Animation: Module("Animation", Category.Render) {

    val mode = ModeValue("Mode", "Normal", arrayOf("Normal", "Exhi", "Sigma", "1.7"))

    init {
        this.noSetEnable = true
    }
}