package cn.rainbow.oxygen.module.modules.movement

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module

class Sprint : Module("Sprint", Category.Movement) {

    @EventTarget(events = [UpdateEvent::class])
    private fun onUpdate(e: Event) {
        if (e is UpdateEvent) {
            if (mc.thePlayer.moveForward > 0.0F && !mc.thePlayer.isSneaking && !mc.thePlayer.isCollidedHorizontally) {
                if(mc.thePlayer.moveForward <= 0.0F && mc.thePlayer.isCollidedVertically){
                    mc.thePlayer.motionX *= 1.185
                    mc.thePlayer.motionZ *= 1.185
                }
                mc.thePlayer.isSprinting = true
            } else {
                mc.thePlayer.isSprinting = false
            }
        }
    }
}