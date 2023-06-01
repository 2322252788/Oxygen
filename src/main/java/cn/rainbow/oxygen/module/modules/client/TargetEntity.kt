package cn.rainbow.oxygen.module.modules.client

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.BooleanValue

@ModuleInfo(name = "TargetEntity", category = Category.Client, noSetEnable = true)
class TargetEntity: Module() {
    val mobs = BooleanValue("Mobs", false)
    val dead = BooleanValue("Dead", false)
    val player = BooleanValue("Players", true)
    val animals = BooleanValue("Animals", false)
    val invis = BooleanValue("Invisibles", false)
}