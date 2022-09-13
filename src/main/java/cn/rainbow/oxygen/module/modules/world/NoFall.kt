package cn.rainbow.oxygen.module.modules.world

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.MotionEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.utils.PlayerUtils

class NoFall: Module("NoFall", Category.World) {

    val modeValue = ModeValue("Mode", "Normal", arrayOf("Normal"))

    @EventTarget(events = [MotionEvent::class])
    fun onEvent(event: Event) {
        if (event is MotionEvent) {
            if (modeValue.isCurrentMode("Normal")) {
                if (mc.thePlayer.fallDistance > 2 || PlayerUtils.getDistanceToFall() > 2) {
                    event.isGround = true
                }
            }
        }
    }
}