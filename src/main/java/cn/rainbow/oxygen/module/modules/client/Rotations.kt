/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.rainbow.oxygen.module.modules.client

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue

object Rotations : Module("Rotations", Category.Client) {
    val headValue = BooleanValue("Head", true)
    val bodyValue = BooleanValue("Body", true)
    val fixedValue = ModeValue("SensitivityFixed", "New", arrayOf("None", "Old", "New"))
    val nanValue = BooleanValue("NaNCheck", true)

    init {
        this.noSetEnable = true
    }
}
