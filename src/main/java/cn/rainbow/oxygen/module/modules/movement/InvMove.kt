package cn.rainbow.oxygen.module.modules.movement

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.ModeValue
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.Item
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C09PacketHeldItemChange
import org.lwjgl.input.Keyboard

class InvMove : Module("InvMove", Category.Movement) {

    val modeValue = ModeValue("Mode", "Normal", arrayOf("Normal", "Hypixel"))

    private var isWalking = false

    @EventTarget(events = [UpdateEvent::class])
    fun onUpdate(event: Event) {
        if (event is UpdateEvent) {
            if (mc.currentScreen != null && mc.currentScreen !is GuiChat) {
                isWalking = true
                if (modeValue.isCurrentMode("Hypixel")) {
                    try {
                        var i = 0
                        for (ii in 0 until 8) {
                            val stack = mc.thePlayer.inventory.getStackInSlot(i) ?: break
                            // 空手
                            if ((stack.item !is ItemFood) && (stack.item !is ItemSword) && Item.getIdFromItem(stack.item) != 345) break // 不能为Food Sword 指南针
                            i++
                        }
                        if (i == 8 && Item.getIdFromItem(mc.thePlayer.inventory.getStackInSlot(8).item) == 345) i--
                        mc.thePlayer.sendQueue.addToSendQueue(C09PacketHeldItemChange(i))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val moveKeys = arrayOf(
                    mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
                    mc.gameSettings.keyBindSprint
                )
                for (bind in moveKeys) {
                    KeyBinding.setKeyBindState(bind.keyCode, Keyboard.isKeyDown(bind.keyCode))
                }
            } else {
                if (isWalking) {
                    if (modeValue.isCurrentMode("Hypixel")) {
                        mc.thePlayer.sendQueue.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                    }
                    isWalking = false
                }
            }
        }
    }

}