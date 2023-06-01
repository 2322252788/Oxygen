package cn.rainbow.oxygen.module.modules.client

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue

@ModuleInfo(name = "Rotations", category = Category.Client, noSetEnable = true)
object Rotations : Module() {
    val headValue = BooleanValue("Head", true)
    val bodyValue = BooleanValue("Body", true)
    val fixedValue = ModeValue("SensitivityFixed", "New", arrayOf("None", "Old", "New"))
    val nanValue = BooleanValue("NaNCheck", true)
}
