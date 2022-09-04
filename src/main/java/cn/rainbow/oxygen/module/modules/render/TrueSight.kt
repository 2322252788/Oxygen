package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue

object TrueSight : Module("TrueSight", Category.Render) {
    @JvmField
	var entities = BooleanValue("Entities", false)
    var barriers = BooleanValue("Barriers", false)
}