package cn.rainbow.oxygen.module.modules.world

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.BooleanValue

@ModuleInfo(name = "Check", category = Category.World)
class Check: Module() {

    companion object {
        @JvmStatic
        val HypixelLobby = BooleanValue("HypixelLobby", false)
    }

    @EventTarget(events = [UpdateEvent::class])
    fun onEvent(event: Event) {
        if (event is UpdateEvent) {

        }
    }
}