package cn.rainbow.oxygen.module.modules.movement

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.MotionEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.modules.combat.KillAura
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.utils.PlayerUtils
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement

class NoSlow: Module("NoSlow", Category.Movement) {

    val packet =  BooleanValue("Packet", false)

    @EventTarget(events = [MotionEvent::class])
    fun onEvent(event: Event) {
        if (event is MotionEvent) {
            if (event.motionType == MotionEvent.MotionType.POST) {
                if (packet.currentValue && mc.thePlayer.isBlocking && PlayerUtils.isMoving() && KillAura.target != null) {
                    mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
                }
            }
        }
    }
}