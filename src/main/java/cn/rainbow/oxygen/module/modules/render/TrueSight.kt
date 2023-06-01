package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.BooleanValue

@ModuleInfo(name = "TrueSight", category = Category.Render)
object TrueSight : Module() {
    @JvmField
	var entities = BooleanValue("Entities", false)
    var barriers = BooleanValue("Barriers", false)
}