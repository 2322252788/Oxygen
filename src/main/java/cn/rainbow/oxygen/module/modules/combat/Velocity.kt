package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.module.setting.NumberValue
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.event.events.PacketEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S27PacketExplosion

class Velocity : Module("Velocity", Category.Combat) {
    private val percentage = NumberValue("Percentage", 0.0, 0.0, 100.0, 5.0)
    private val horizontalValue = NumberValue("Horizontal", 0.0, 0.0, 1.0, 0.01)
    private val verticalValue = NumberValue("Vertical", 0.0, 0.0, 1.0, 0.01)
    private val mode = ModeValue("Mode", "Normal")

    init {
        mode.addValue("Normal")
        mode.addValue("Simple")
        mode.addValue("AAC")
        mode.addValue("AAC2")
    }

    @EventTarget(events = [PacketEvent::class])
    private fun onPacketReceive(event: Event) {
        displayName = mode.currentValue
        if (event is PacketEvent) {
            if (event.packetType == PacketEvent.PacketType.Recieve) {
                if (mode.isCurrentMode("AAC")) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= 0.6
                        mc.thePlayer.motionZ *= 0.6
                    } else {
                        mc.thePlayer.motionX *= 1.0
                        mc.thePlayer.motionZ *= 1.0
                    }
                }
                if (event.getPacket() is S12PacketEntityVelocity || event.getPacket() is S27PacketExplosion) {
                    when (mode.currentValue) {
                        "Normal" -> {
                            event.isCancelled = true
                        }
                        "Simple" -> {
                            val horizontal = horizontalValue.currentValue
                            val vertical = verticalValue.currentValue
                            if (horizontal == 0.0 && vertical == 0.0) {
                                event.isCancelled = true
                            }
                            val packet = event.getPacket() as S12PacketEntityVelocity
                            packet.motionX = (mc.thePlayer.motionX * horizontal).toInt()
                            packet.motionY = (mc.thePlayer.motionY * vertical).toInt()
                            packet.motionZ = (mc.thePlayer.motionZ * horizontal).toInt()
                        }
                        "AAC2" -> if (mc.thePlayer.onGround) {
                            if (percentage.currentValue == 0.0) {
                                event.setCancelled(true)
                            } else {
                                val packet2 = event.getPacket() as S12PacketEntityVelocity
                                packet2.motionX = (percentage.currentValue / 100.0).toInt()
                                packet2.motionY = (percentage.currentValue / 100.0).toInt()
                                packet2.motionZ = (percentage.currentValue / 100.0).toInt()
                            }
                        }
                    }
                }
            }
        }
    }
}