package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.PacketEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.modules.movement.Speed
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.NumberValue
import net.minecraft.entity.Entity
import net.minecraft.item.EnumAction
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import java.util.concurrent.ThreadLocalRandom

@ModuleInfo(name = "Criticals", category = Category.Combat)
class Criticals : Module() {

    private val mode = ModeValue("Mode", "Packet", arrayOf("Packet", "Packet2", "Packet3", "Jump"))
    private val hurtTime = NumberValue("HurtTime", 15.0, 10.0, 20.0, 1.0)

    private var lY = -1.0

    private fun doCrits(offsets: DoubleArray) {
        for (i in offsets.indices)
            mc.thePlayer.sendQueue.addToSendQueue(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY + offsets[i], mc.thePlayer.posZ, false
                )
            )
    }

    private val isEating: Boolean
        get() = mc.thePlayer.isUsingItem && mc.thePlayer.itemInUse.item
            .getItemUseAction(mc.thePlayer.itemInUse) == EnumAction.EAT

    @EventTarget(events = [PacketEvent::class])
    fun onEvent(event: Event) {
        if (event is PacketEvent) {
            val packet = event.getPacket()
            if (packet is C03PacketPlayer) {
                lY = packet.positionY
            }
            if (packet !is C02PacketUseEntity) return
            if (isEating || Oxygen.INSTANCE.moduleManager.getModule(Speed::class.java)!!.enabled) return
            if (packet.action == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically &&
                hurtTimeCheck(packet.getEntityFromWorld(mc.theWorld))) {
                if (mode.isCurrentMode("Packet")) {
                    doCrits(doubleArrayOf(0.06, 0.0, 0.03, 0.0))
                }
                if (mode.isCurrentMode("Packet2")) {
                    doCrits(doubleArrayOf(0.063, 0.0, 0.000111, 0.0))
                }
                if (mode.isCurrentMode("Packet3")) {
                    doCrits(doubleArrayOf(0.051 * randomDouble(1.08, 1.1), 0.0, 0.0125 * randomDouble(1.01, 1.07), 0.0))
                }
                if (mode.isCurrentMode("Jump")) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump()
                    }
                }
            }
        }
    }

    private fun randomDouble(min: Double, max: Double): Double {
        return ThreadLocalRandom.current().nextDouble(min, max)
    }

    private fun hurtTimeCheck(entity: Entity): Boolean {
        return entity.hurtResistantTime <= hurtTime.currentValue
    }
}