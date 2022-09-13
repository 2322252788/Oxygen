package cn.rainbow.oxygen.module.modules.player

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.NumberValue
import cn.rainbow.oxygen.utils.timer.MSTimer
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.C03PacketPlayer

class FastUse : Module("FastUse", Category.Player) {

    private val modeValue = ModeValue("Mode", "DelayedInstant", arrayOf("NCP","Instant", "Timer", "CustomDelay", "DelayedInstant", "MinemoraTest", "AAC", "NewAAC","Medusa","Matrix"))
    private val timerValue = NumberValue("Timer", 1.22, 0.1, 2.0, 0.1)
    private val durationValue = NumberValue("InstantDelay", 14.0, 0.0, 35.0, 1.0)
    private val delayValue = NumberValue("CustomDelay", 0.0, 0.0, 300.0, 1.0)

    private val msTimer = MSTimer()
    private var usedTimer = false

    @EventTarget(events = [UpdateEvent::class])
    fun onUpdate(event: Event) {
        if (event is UpdateEvent) {
            if (usedTimer) {
                mc.timer.timerSpeed = 1F
                usedTimer = false
            }

            if (!mc.thePlayer.isUsingItem) {
                return
            }

            val usingItem = mc.thePlayer.itemInUse.item

            if (usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion) {
                when (modeValue.currentValue.lowercase()) {
                    "matrix" -> {
                        mc.timer.timerSpeed = 0.5f
                        usedTimer = true
                        repeat(1) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }
                    }
                    "medusa" -> {
                        if (mc.thePlayer.itemInUseDuration > 5 || !msTimer.hasTimePassed(360L))
                            return

                        repeat(20) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }

                        msTimer.reset()
                    }
                    "delayedinstant" -> if (mc.thePlayer.itemInUseDuration > durationValue.currentValue) {
                        repeat(36 - mc.thePlayer.itemInUseDuration) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }

                        mc.playerController.onStoppedUsingItem(mc.thePlayer)
                    }

                    "ncp" -> if (mc.thePlayer.itemInUseDuration > 14) {
                        repeat(20) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }

                        mc.playerController.onStoppedUsingItem(mc.thePlayer)}

                    "instant" -> {
                        repeat(35) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }

                        mc.playerController.onStoppedUsingItem(mc.thePlayer)
                    }
                    "aac" -> {
                        mc.timer.timerSpeed = 0.49F
                        usedTimer = true
                        if (mc.thePlayer.itemInUseDuration > 14) {
                            repeat(23) {
                                mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                            }
                        }
                    }
                    "newaac" -> {
                        mc.timer.timerSpeed = 0.49F
                        usedTimer = true
                        repeat(2) {
                            mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        }

                        // mc.playerController.onStoppedUsingItem(mc.thePlayer)
                    }
                    "timer" -> {
                        mc.timer.timerSpeed = timerValue.currentValue.toFloat()
                        usedTimer = true
                    }

                    "minemoratest" -> {
                        mc.timer.timerSpeed = 0.5F
                        usedTimer = true
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            repeat(2) {
                                mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                            }
                        }
                    }

                    "customdelay" -> {
                        if (!msTimer.hasTimePassed(delayValue.currentValue.toLong())) {
                            return
                        }

                        mc.netHandler.addToSendQueue(C03PacketPlayer(mc.thePlayer.onGround))
                        msTimer.reset()
                    }
                }
            }
        }
    }

    // @EventTarget
    // fun onMove(event: MoveEvent?) {
    //     if (event == null) return

    //     if (!mc.thePlayer.isUsingItem || !modeValue.get().lowercase()=="aac") return
    //     val usingItem1 = mc.thePlayer.itemInUse.item
    //     if ((usingItem1 is ItemFood || usingItem1 is ItemBucketMilk || usingItem1 is ItemPotion))
    //         event.zero()
    // }

    override fun onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }
    }
}